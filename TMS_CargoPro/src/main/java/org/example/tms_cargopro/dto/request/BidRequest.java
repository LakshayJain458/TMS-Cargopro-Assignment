package org.example.tms_cargopro.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidRequest {

    @NotNull(message = "Load ID is required")
    private UUID loadId;

    @NotNull(message = "Transporter ID is required")
    private UUID transporterId;

    @NotNull(message = "Proposed rate is required")
    @Positive(message = "Proposed rate must be positive")
    private Double proposedRate;

    @NotNull(message = "Trucks offered is required")
    @Positive(message = "Trucks offered must be positive")
    private Integer trucksOffered;
}

