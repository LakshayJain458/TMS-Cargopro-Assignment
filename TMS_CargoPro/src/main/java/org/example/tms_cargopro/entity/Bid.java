package org.example.tms_cargopro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms_cargopro.enums.BidStatus;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bids",
        indexes = {
                @Index(name = "idx_bid_id", columnList = "bid_id"),
                @Index(name = "idx_load_id", columnList = "load_id"),
                @Index(name = "idx_transporter_id", columnList = "transporter_id"),
                @Index(name = "idx_bid_status", columnList = "status")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "bid_id", updatable = false, nullable = false)
    private UUID bidId;

    @Column(name = "load_id", nullable = false)
    private UUID loadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "load_id", referencedColumnName = "load_id",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_bid_load"))
    private Load load;

    @Column(name = "transporter_id", nullable = false)
    private UUID transporterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transporter_id", referencedColumnName = "transporter_id",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_bid_transporter"))
    private Transporter transporter;

    @Column(name = "proposed_rate", nullable = false)
    private Double proposedRate;

    @Column(name = "trucks_offered", nullable = false)
    private Integer trucksOffered;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BidStatus status;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Version
    @Column(nullable = false)
    private Long version;

    @PrePersist
    protected void onCreate() {
        if (submittedAt == null) {
            submittedAt = Instant.now();
        }
        if (status == null) {
            status = BidStatus.PENDING;
        }
    }
}

