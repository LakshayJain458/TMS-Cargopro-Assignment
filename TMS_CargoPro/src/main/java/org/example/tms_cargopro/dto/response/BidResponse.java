package org.example.tms_cargopro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms_cargopro.enums.BidStatus;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidResponse {

    private UUID bidId;
    private UUID loadId;
    private UUID transporterId;
    private Double proposedRate;
    private Integer trucksOffered;
    private BidStatus status;
    private Instant submittedAt;
}

