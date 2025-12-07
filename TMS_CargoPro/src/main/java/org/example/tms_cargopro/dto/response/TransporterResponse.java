package org.example.tms_cargopro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms_cargopro.entity.AvailableTruck;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransporterResponse {

    private UUID transporterId;
    private String companyName;
    private Double rating;
    private List<AvailableTruck> availableTrucks;
}

