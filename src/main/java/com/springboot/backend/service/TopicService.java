package com.springboot.backend.service;

import com.springboot.backend.model.Topics;
import com.springboot.backend.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {
    public Topics generateTopics(long fieldId, String deviceUuid) {
        Topics topic = new Topics();
        topic.setControlTopic(fieldId + "/" + deviceUuid + "/control");
        topic.setDataTopic(fieldId + "/" + deviceUuid + "/data");
        topic.setNotificationTopic(fieldId + "/" + deviceUuid + "/notification");
        topic.setDeviceStateTopic(fieldId + "/" + deviceUuid + "/deviceState");
        topic.setConfigurationReadyTopic(fieldId + "/" + deviceUuid + "/configurationReady");
        topic.setIrrigationOverrideTopic(fieldId + "/" + deviceUuid + "/irrigationOverride");

        return topic;
    }
}
