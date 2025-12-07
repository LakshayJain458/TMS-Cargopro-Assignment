package org.example.tms_cargopro.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms_cargopro.enums.WeightUnit;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadRequest {

    @NotBlank(message = "Shipper ID is required")
    private String shipperId;

    @NotBlank(message = "Loading city is required")
    private String loadingCity;

    @NotBlank(message = "Unloading city is required")
    private String unloadingCity;

    @NotNull(message = "Loading date is required")
    private Instant loadingDate;

    @NotBlank(message = "Product type is required")
    private String productType;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private Double weight;

    @NotNull(message = "Weight unit is required")
    private WeightUnit weightUnit;

    @NotNull(message = "Number of trucks is required")
    @Positive(message = "Number of trucks must be positive")
    private Integer noOfTrucks;

    @NotBlank(message = "Truck type is required")
    private String truckType;

}
