package org.example.tms_cargopro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestBidResponse {

    private UUID bidId;
    private UUID loadId;
    private UUID transporterId;
    private String companyName;
    private Double proposedRate;
    private Integer trucksOffered;
    private Double transporterRating;
    private Double score;
}

