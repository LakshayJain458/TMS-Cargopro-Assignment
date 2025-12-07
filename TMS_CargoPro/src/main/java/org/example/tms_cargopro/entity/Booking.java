package org.example.tms_cargopro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms_cargopro.enums.BookingStatus;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_booking_load_id", columnList = "load_id"),
        @Index(name = "idx_booking_transporter_id", columnList = "transporter_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id", updatable = false, nullable = false)
    private UUID bookingId;

    @Column(name = "load_id", nullable = false)
    private UUID loadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "load_id", referencedColumnName = "load_id",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_booking_load"))
    private Load load;

    @Column(name = "bid_id", nullable = false)
    private UUID bidId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid_id", referencedColumnName = "bid_id",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_booking_bid"))
    private Bid bid;

    @Column(name = "transporter_id", nullable = false)
    private UUID transporterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transporter_id", referencedColumnName = "transporter_id",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_booking_transporter"))
    private Transporter transporter;

    @Column(name = "allocated_trucks", nullable = false)
    private Integer allocatedTrucks;

    @Column(name = "final_rate", nullable = false)
    private Double finalRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(name = "booked_at", nullable = false)
    private Instant bookedAt;

    @Version
    @Column(nullable = false)
    private Long version;

    @PrePersist
    protected void onCreate() {
        if (bookedAt == null) {
            bookedAt = Instant.now();
        }
        if (status == null) {
            status = BookingStatus.CONFIRMED;
        }
    }
}

