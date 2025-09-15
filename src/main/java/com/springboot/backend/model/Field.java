package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_fields")
public class Field {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id", nullable = false)
    private long id;
    private String field_name;
    private String field_location;
    private double field_size;
    private String field_size_unit;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    // Keep this collection LAZY and mapped by the owning side's field property
    @OneToMany(mappedBy = "field", fetch = FetchType.LAZY)
    @JsonIgnore   // prevents Jackson from walking into the collection
    private List<Area> areas = new ArrayList<>();
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "irrigation_plan_id", referencedColumnName = "irrigation_plan_id")
    private IrrigationPlan irrigationPlan;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deviceID", referencedColumnName = "deviceID")
    private Device device;

    private int delete_status = 0;
}
