package org.example.tms_cargopro.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms_cargopro.entity.AvailableTruck;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrucksRequest {

    @NotNull(message = "Available trucks list is required")
    @Valid
    private List<AvailableTruck> availableTrucks;
}

