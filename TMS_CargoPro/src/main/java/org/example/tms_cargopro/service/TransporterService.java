package org.example.tms_cargopro.service;

import org.example.tms_cargopro.dto.request.TransporterRequest;
import org.example.tms_cargopro.dto.request.UpdateTrucksRequest;
import org.example.tms_cargopro.dto.response.TransporterResponse;

import java.util.UUID;

public interface TransporterService {

    TransporterResponse createTransporter(TransporterRequest request);

    TransporterResponse getTransporterById(UUID transporterId);

    TransporterResponse updateTrucks(UUID transporterId, UpdateTrucksRequest request);

    void deductTrucks(UUID transporterId, String truckType, int count);

    void restoreTrucks(UUID transporterId, String truckType, int count);

    int getAvailableTruckCount(UUID transporterId, String truckType);
}

