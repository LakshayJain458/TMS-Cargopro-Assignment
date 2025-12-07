package org.example.tms_cargopro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms_cargopro.enums.LoadStatus;
import org.example.tms_cargopro.enums.WeightUnit;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "loads",
        indexes = {
                @Index(name = "idx_status", columnList = "status"),
                @Index(name = "idx_shipper_id", columnList = "shipper_id")
        }
)
public class Load {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "load_id", updatable = false, nullable = false)
    private UUID loadId;

    @Column(name = "shipper_id", nullable = false)
    private String shipperId;

    @Column(name = "loading_city", nullable = false)
    private String loadingCity;

    @Column(name = "unloading_city", nullable = false)
    private String unloadingCity;

    @Column(name = "loading_date", nullable = false)
    private Instant loadingDate;

    @Column(name = "product_type", nullable = false)
    private String productType;

    @Column(nullable = false)
    private Double weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "weight_unit", nullable = false)
    private WeightUnit weightUnit;

    @Column(name = "truck_type", nullable = false)
    private String truckType;

    @Column(name = "no_of_trucks", nullable = false)
    private Integer noOfTrucks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoadStatus status;

    @Column(name = "date_posted", nullable = false)
    private Instant datePosted;

    @Version
    @Column(nullable = false)
    private Long version;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = LoadStatus.POSTED;
        }
        if (datePosted == null) {
            datePosted = Instant.now();
        }
    }
}
