package com.springboot.backend.MQTT;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.backend.model.DataResponse;
import com.springboot.backend.model.Field;
import com.springboot.backend.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MqttSubscriber implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(MqttSubscriber.class);
    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]");

    @Autowired
    private static JdbcTemplate jdbcTemplate;
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/moroka?autoReconnect=true&useSSL=false";
    private static final String MYSQL_USER = "admin";
    private static final String MYSQL_PASS = "moroka"; // TODO: move to properties

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectMapper mapper = new ObjectMapper();


    @Autowired private DeviceTypeRepository deviceTypeRepository;
    @Autowired private static DeviceRepository deviceRepository;
    @Autowired private DeviceSubTypeRepository deviceSubTypeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private static FieldRepository fieldRepository;
    @Autowired private static DataResponseRepository dataResponseRepository;
    @Autowired private SensorDataRepository sensorDataRepository;

    private ApplicationContext applicationContext;
    private static MqttClient mqttClient = null;

    public MqttSubscriber(DeviceRepository deviceRepository,
                          MqttClient mqttClient,
                          FieldRepository fieldRepository,
                          DataResponseRepository dataResponseRepository,
                          SensorDataRepository sensorDataRepository, JdbcTemplate jdbcTemplate) throws SQLException {
        MqttSubscriber.deviceRepository = deviceRepository;
        MqttSubscriber.fieldRepository = fieldRepository;
        MqttSubscriber.dataResponseRepository = dataResponseRepository;
        this.sensorDataRepository = sensorDataRepository;
        MqttSubscriber.mqttClient = mqttClient;
        this.jdbcTemplate = jdbcTemplate; // ✅ now never null

    }

    @PostConstruct
    public void subscribeToDeviceTopics() {
        try {
            if (mqttClient == null) {
                log.error("MQTT client is null. Check your @Bean configuration.");
                return;
            }
            if (!mqttClient.isConnected()) {
                try {
                    mqttClient.connect();
                    log.info("Connected to MQTT broker: {}", mqttClient.getServerURI());
                } catch (MqttException e) {
                    log.error("Failed to connect to MQTT broker: {}", e.getMessage(), e);
                    return;
                }
            }

            // Subscribe to everything and route in code (adjust if you want per-topic)
            subscribeToTopic("#");
        } catch (Exception e) {
            log.error("Unexpected error during MQTT subscription setup", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private static boolean isCompleteTopic(String topic) {
        // Format: {fieldId}/{deviceId}/{suffix}
        return topic != null && topic.split("/").length >= 3;
    }

    public static void subscribeToTopic(String topic) {
        if (mqttClient == null) {
            log.error("Cannot subscribe; mqttClient is null");
            return;
        }
        try {
            mqttClient.subscribe(topic, (t, msg) -> {
                final String message = new String(msg.getPayload(), StandardCharsets.UTF_8);
                try {
                    if (t.endsWith("/control")) {
                        handleControlMessage(t, message);
                    } else if (t.endsWith("/data")) {
                        handleDataMessage(t, message);
                    } else if (t.endsWith("/notification")) {
                        handleNotificationMessage(t, message);
                    } else if (t.endsWith("/deviceState")) {
                        handleDeviceStateMessage(t, message);
                    } else if (t.endsWith("/configurationReady")) {
                        handleConfigurationReadyMessage(t, message);
                    } else {
                        log.debug("Message on unrelated topic ignored: topic={}", t);
                    }
                } catch (Throwable ex) {
                    // Never let exceptions escape the MQTT callback
                    log.error("Unhandled exception processing MQTT message. topic={}, payload={}");
                          //  t, TopicParts.trimForLog(message), ex);
                }
            });
            log.info("Subscribed to topic: {}", topic);
        } catch (MqttException e) {
            log.error("MQTT subscribe failed for topic '{}': {}", topic, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error subscribing to topic '{}'", topic, e);
        }
    }

    // ---------- CONTROL ----------
    private static void handleControlMessage(String topic, String message) {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]");

        // Call this when you receive an MQTT message on a /data topic



        final var parts = safeTopicParts(topic).orElse(null);
        if (parts == null) {
            log.warn("Invalid control topic format: {}", topic);
            return;
        }
        final long fieldId = parts.fieldId();
        try {
            JSONObject json = new JSONObject(message);
            String command = json.optString("command", "");
            long deviceID = json.optLong("deviceID", -1);
            long controlState = json.optLong("controlState", 0);

            if (!"deviceControlResponse".equalsIgnoreCase(command)) {
                log.debug("Ignoring control message with command='{}' for deviceID={}", command, deviceID);
                return;
            }

            ensureFieldExists(fieldId);

            DataResponse responseID = dataResponseRepository.findLatestIdByDeviceId(deviceID);
            if (responseID == null) {
                log.warn("No DataResponse found for deviceID={} (cannot update device_status)", deviceID);
                return;
            }

            try (Connection con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
                 PreparedStatement ps = con.prepareStatement(
                         "UPDATE tbl_devices SET device_status = ? WHERE deviceResponseID = ?")) {
                ps.setLong(1, controlState);
                ps.setLong(2, responseID.getId());
                int rows = ps.executeUpdate();
                log.info("Control update applied: deviceID={}, controlState={}, rows={}", deviceID, controlState, rows);
            }
        } catch (EntityNotFoundException enf) {
            log.error("Field not found while handling control. fieldId={}, topic={}", fieldId, topic, enf);
        } catch (org.json.JSONException je) {
            log.error("Invalid JSON payload (control). topic={}, payload={}", topic, trimForLog(message), je);
        } catch (SQLException se) {
            log.error("SQL error (control). topic={}, fieldId={}, payload={}", topic, fieldId, trimForLog(message), se);
        } catch (Exception e) {
            log.error("Unexpected error (control). topic={}, payload={}", topic, trimForLog(message), e);
        }
    }

    // ---------- DATA ----------
    // ---- DATA (single, consistent implementation for all deviceTypeIDs) ----
    private static void handleDataMessage(String topic, String message) throws JsonProcessingException {
        final var parts = safeTopicParts(topic).orElse(null);

        final long fieldId = parts.fieldId();
        JsonNode root = mapper.readTree(message);

        try {
            JSONObject json = new JSONObject(message);

            final String command = json.optString("command", "");
            if (!"deviceDataResponse".equalsIgnoreCase(command)) {
                log.debug("Ignoring data message with command='{}'", command);
                return;
            }

       // ensureFieldExists(fieldId);

            final String deviceName     = json.optString("deviceName", "");
            final long   deviceID       = json.optLong("deviceID", -1);
            final int    deviceTypeID   = json.optInt("deviceTypeID", 0);
            final int    deviceSubTypeID= json.optInt("deviceSubTypeID", 0);
            final int    batteryLevel   = json.optInt("batteryLevel", 0);
            final int    deviceState    = json.optInt("deviceState", 0);
            final int    controlState   = json.optInt("controlState", 0);
            final String timeStampStr   = json.optString("timeStamp", null);

            final Integer areaId  = null;  // set if you actually have it
            final Long    patchId = null;  // set if you actually have it

            log.debug("Data msg -> topic={}, deviceId={}, type={}, subType={}, battery={}, state={}, control={}, fieldId={}, ts={}",
                    topic, deviceID, deviceTypeID, deviceSubTypeID, batteryLevel, deviceState, controlState, fieldId, timeStampStr);

            // 1) Insert into tbl_data_response using explicit column order (matches your schema)
            insertDataResponseRow(
                    batteryLevel, command, controlState, deviceID, deviceName,
                    deviceState, deviceSubTypeID, deviceTypeID, timeStampStr,
                    areaId, fieldId, patchId
            );

            // 2) Link latest DataResponse to device (tbl_devices.deviceResponseID)
            linkLatestDataResponseToDevice(deviceID);
            if (deviceTypeID==2){

                // Parse timestamp (handles "yyyy-MM-dd HH:mm:ss" and "yyyy-MM-dd HH:mm:ss.SSS")

                LocalDateTime ldt = LocalDateTime.parse(timeStampStr, TS_FMT);
                Timestamp ts = Timestamp.valueOf(ldt);

// ...inside handleDeviceDataResponse(...)

                JsonNode sensorData = root.path("sensorData");
                if (!sensorData.isArray()) return;

// Collect rows into a strongly-typed list
                List<JsonNode> rows = new ArrayList<>();
                sensorData.forEach(rows::add);

                final String sql = """
                    INSERT INTO tbl_sensor_data
                      (ec, tds, device_id, layer_level, moisture, nitrogen, phosphorus, potassium, salinity, temperature, timestamp)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;

// Explicitly type the setter to JsonNode so .path(...) resolves
                jdbcTemplate.batchUpdate(sql, rows, 100,
                        (PreparedStatement ps, JsonNode node) -> {
                            ps.setString(1, node.path("EC").asText(null));
                            ps.setString(2, node.path("TDS").asText(null));
                            ps.setString(3, String.valueOf(deviceID));
                            ps.setString(4, node.path("LayerLevel").asText(null));
                            ps.setString(5, node.path("Moisture").asText(null));
                            ps.setString(6, node.path("Nitrogen").asText(null));
                            ps.setString(7, node.path("Phosphorus").asText(null));
                            ps.setString(8, node.path("Potassium").asText(null));
                            ps.setString(9, node.path("Salinity").asText(null));
                            ps.setString(10, node.path("Temperature").asText(null));
                            ps.setTimestamp(11, ts);
                        }
                );

            }

            // 3) Type-specific extras
            if (deviceTypeID == 4) {
                // Flow meter array
                JSONArray sensorData = json.optJSONArray("sensorData");
                if (sensorData != null) {
                    for (int i = 0; i < sensorData.length(); i++) {
                        JSONObject d = sensorData.optJSONObject(i);
                        if (d == null) continue;
                        double flowRate       = parseDoubleSafe(d.optString("FlowRate", "0"), 0.0);
                        double dispensedWater = parseDoubleSafe(d.optString("DispensedWater", "0"), 0.0);
                        insertFlowMeterRow(flowRate, dispensedWater, deviceID, timeStampStr);
                    }
                }
            }

            log.info("Data handled successfully for deviceId={}, deviceTypeID={}", deviceID, deviceTypeID);

        } catch (EntityNotFoundException enf) {
            log.error("Field not found while handling data. fieldId={}, topic={}", fieldId, topic, enf);
        } catch (org.json.JSONException je) {
            log.error("Invalid JSON payload (data). topic={}, payload={}", topic, trimForLog(message), je);
        } catch (SQLException se) {
            log.error("SQL error (data). topic={}, fieldId={}, payload={}", topic, fieldId, trimForLog(message), se);
        } catch (Exception e) {
            log.error("Unexpected error (data). topic={}, payload={}", topic, trimForLog(message), e);
        }
    }

    /**
     * Inserts a row into tbl_data_response using the EXACT column order from your schema:
     * 1 id(AI) | 2 battery_level | 3 command | 4 control_state | 5 device_id | 6 device_name |
     * 7 device_state | 8 device_sub_type_id | 9 device_type_id | 10 time_stamp | 11 area_id |
     * 12 field_id | 13 patch_id
     */
    private static void insertDataResponseRow(
            int batteryLevel, String command, int controlState, long deviceId, String deviceName,
            int deviceState, int deviceSubTypeId, int deviceTypeId, String timeStampStr,
            Integer areaId, long fieldId, Long patchId
    ) throws SQLException {
        final String sql = """
        INSERT INTO tbl_data_response (
          battery_level,
          command,
          control_state,
          device_id,
          device_name,
          device_state,
          device_sub_type_id,
          device_type_id,
          time_stamp,
          area_id,
          field_id,
          patch_id
        ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
        """;

        try (Connection con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, batteryLevel);
            ps.setString(2, command);
            ps.setInt(3, controlState);
            ps.setLong(4, deviceId);
            ps.setString(5, deviceName);
            ps.setInt(6, deviceState);
            ps.setInt(7, deviceSubTypeId);
            ps.setInt(8, deviceTypeId);
            ps.setString(9, timeStampStr); // column is VARCHAR(255) in your schema
            if (areaId == null) ps.setNull(10, java.sql.Types.INTEGER); else ps.setInt(10, areaId);
            ps.setLong(11, fieldId);
            if (patchId == null) ps.setNull(12, java.sql.Types.BIGINT); else ps.setLong(12, patchId);

            int rows = ps.executeUpdate();
            log.debug("Inserted tbl_data_response rows={}, deviceId={}", rows, deviceId);
        }
    }

    /** After inserting data_response, link the latest row to tbl_devices.deviceResponseID */
    private static void linkLatestDataResponseToDevice(long deviceId) throws SQLException {
        DataResponse latest = dataResponseRepository.findLatestIdByDeviceId(deviceId);
        if (latest == null) {
            log.warn("No DataResponse found to link for deviceId={}", deviceId);
            return;
        }
        final String updSql = "UPDATE tbl_devices SET deviceResponseID = ? WHERE device_uuid = ?";
        try (Connection con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
             PreparedStatement upd = con.prepareStatement(updSql)) {
            upd.setLong(1, latest.getId());
            upd.setLong(2, deviceId);
            int u = upd.executeUpdate();
            log.debug("Linked DataResponse -> device: deviceId={}, dataResponseId={}, rows={}", deviceId, latest.getId(), u);
        }
    }

    /** Insert into tbl_flow_meter with explicit columns (adjust names if different) */
    private static void insertFlowMeterRow(double flowRate, double dispensedWater, long deviceUuid, String timeStampStr)
            throws SQLException {
        final String sql = """
        INSERT INTO tbl_flow_meter (
          flow_rate,
          dispensed_water,
          device_id,
          timestamp
        ) VALUES (?,?,?,?)
        """;
        try (Connection con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, flowRate);
            ps.setDouble(2, dispensedWater);
            ps.setLong(3, deviceUuid);
            ps.setString(4, timeStampStr); // keep as string to match your schema
            int rows = ps.executeUpdate();
            log.debug("Inserted tbl_flow_meter rows={}, deviceUuid={}, flowRate={}, dispensed={}",
                    rows, deviceUuid, flowRate, dispensedWater);
        }
    }


    private static void insertDataResponse_Generic(long fieldId, String deviceName, long device_uuid, long deviceTypeID,
                                                   long deviceSubTypeID, long batteryLevel, long deviceState,
                                                   long controlState, Timestamp timestamp) throws SQLException {
        try (Connection con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO tbl_data_response VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
            long id = 0, patch_id = 0, area_id = 0;
            ps.setLong(1, id);
            ps.setString(2, deviceName);
            ps.setLong(3, device_uuid);
            ps.setLong(4, deviceTypeID);
            ps.setLong(5, deviceSubTypeID);
            ps.setLong(6, batteryLevel);
            ps.setLong(7, deviceState);
            ps.setLong(8, controlState);
            ps.setString(9, "deviceDataResponse");
            ps.setLong(10, area_id);
            ps.setLong(11, fieldId);
            ps.setLong(12, patch_id);
            ps.setTimestamp(13, timestamp != null ? timestamp : new Timestamp(System.currentTimeMillis()));
            int rows = ps.executeUpdate();
            log.info("Inserted tbl_data_response (generic) rows={}; device_id={}", rows, device_uuid);

            DataResponse dr = dataResponseRepository.findLatestIdByDeviceId(device_uuid);
            if (dr == null) {
                log.warn("DataResponse not found after insert for device_uuid={}", device_uuid);
                return;
            }
            try (PreparedStatement upd = con.prepareStatement(
                    "UPDATE tbl_devices SET deviceResponseID = ? WHERE device_uuid = ?")) {
                upd.setLong(1, dr.getId());
                upd.setLong(2, device_uuid);
                int urows = upd.executeUpdate();
                log.debug("Linked DataResponse to device (generic). device_id={}, dataResponseID={}, rows={}",
                        device_uuid, dr.getId(), urows);
            }
        }
    }

    // ---------- OTHER MESSAGE TYPES ----------
    private static void handleNotificationMessage(String topic, String message) {
        final var parts = safeTopicParts(topic).orElse(null);
        if (parts == null) {
            log.warn("Invalid notification topic format: {}", topic);
            return;
        }
        log.info("Notification received: fieldId={}, deviceId={}, payload={}",
                parts.fieldId(), parts.deviceId(), trimForLog(message));
        // TODO: persist if needed
    }

    private static void handleDeviceStateMessage(String topic, String message) {
        final var parts = safeTopicParts(topic).orElse(null);
        if (parts == null) {
            log.warn("Invalid deviceState topic format: {}", topic);
            return;
        }
        log.debug("DeviceState received: fieldId={}, deviceId={}, payload={}",
                parts.fieldId(), parts.deviceId(), trimForLog(message));
        // TODO: persist or update cache if needed
    }

    private static void handleConfigurationReadyMessage(String topic, String message) {
        final var parts = safeTopicParts(topic).orElse(null);
        if (parts == null) {
            log.warn("Invalid configurationReady topic format: {}", topic);
            return;
        }
        log.info("Configuration ready: fieldId={}, deviceId={}", parts.fieldId(), parts.deviceId());
    }

    // ---------- UTILITIES ----------
    private record TopicParts(long fieldId, long deviceId, String suffix) {}

    private static Optional<TopicParts> safeTopicParts(String topic) {
        if (topic == null) return Optional.empty();
        String[] arr = topic.split("/");
        if (arr.length < 3) return Optional.empty();
        try {
            long fieldId = Long.parseLong(arr[0]);
            long deviceId = Long.parseLong(arr[1]);
            String suffix = arr[2];
            return Optional.of(new TopicParts(fieldId, deviceId, suffix));
        } catch (NumberFormatException nfe) {
            return Optional.empty();
        }
    }

    private static void ensureFieldExists(long fieldId) {
        fieldRepository.findById(fieldId)
                .orElseThrow(() -> new EntityNotFoundException("Field not found with ID: " + fieldId));
    }

    private static Timestamp parseTimestamp(String ts) {
        if (ts == null || ts.isBlank()) return null;
        try {
            return Timestamp.valueOf(ts); // expects "yyyy-MM-dd HH:mm:ss"
        } catch (IllegalArgumentException iae) {
            log.warn("Invalid timeStamp format '{}', using current time", ts);
            return null;
        }
    }

    private static double parseDoubleSafe(String s, double def) {
        try { return (s == null || s.isBlank()) ? def : Double.parseDouble(s); }
        catch (Exception e) { return def; }
    }

    static String trimForLog(String s) {
        if (s == null) return "";
        return s.length() <= 1000 ? s : s.substring(0, 1000) + "...[truncated]";
    }
}
