package org.example.tms_cargopro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "transporters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transporter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transporter_id", updatable = false, nullable = false)
    private UUID transporterId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(nullable = false)
    private Double rating = 3.0;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "transporter_trucks",
            joinColumns = @JoinColumn(name = "transporter_id"))
    private List<AvailableTruck> availableTrucks = new ArrayList<>();

    @Version
    @Column(nullable = false)
    private Long version;

    @PrePersist
    protected void onCreate() {
        if (rating == null) {
            rating = 3.0;
        }
    }
}

