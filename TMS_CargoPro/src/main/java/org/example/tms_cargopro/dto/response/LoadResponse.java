package org.example.tms_cargopro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms_cargopro.enums.LoadStatus;
import org.example.tms_cargopro.enums.WeightUnit;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoadResponse {
    private UUID loadId;
    private String shipperId;
    private String loadingCity;
    private String unloadingCity;
    private Instant loadingDate;
    private String productType;
    private Double weight;
    private WeightUnit weightUnit;
    private String truckType;
    private Integer noOfTrucks;
    private LoadStatus status;
    private Instant datePosted;
    private Long version;
}



