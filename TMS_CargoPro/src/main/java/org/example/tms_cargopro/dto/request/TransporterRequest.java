package org.example.tms_cargopro.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms_cargopro.entity.AvailableTruck;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransporterRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5.0")
    private Double rating;

    @NotNull(message = "Available trucks list is required")
    @Valid
    private List<AvailableTruck> availableTrucks;
}

