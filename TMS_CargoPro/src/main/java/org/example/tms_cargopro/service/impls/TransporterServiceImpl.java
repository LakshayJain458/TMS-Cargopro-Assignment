package org.example.tms_cargopro.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms_cargopro.dto.request.TransporterRequest;
import org.example.tms_cargopro.dto.request.UpdateTrucksRequest;
import org.example.tms_cargopro.dto.response.TransporterResponse;
import org.example.tms_cargopro.entity.AvailableTruck;
import org.example.tms_cargopro.entity.Transporter;
import org.example.tms_cargopro.exception.ResourceNotFoundException;
import org.example.tms_cargopro.mapper.EntityMapper;
import org.example.tms_cargopro.repository.TransporterRepository;
import org.example.tms_cargopro.service.TransporterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransporterServiceImpl implements TransporterService {

    private final TransporterRepository transporterRepository;

    @Transactional
    @Override
    public TransporterResponse createTransporter(TransporterRequest request) {
        log.info("Creating new transporter: {}", request.getCompanyName());
        Transporter transporter = EntityMapper.toEntity(request);
        Transporter savedTransporter = transporterRepository.save(transporter);
        log.info("Transporter created with ID: {}", savedTransporter.getTransporterId());
        return EntityMapper.toResponse(savedTransporter);
    }

    @Transactional(readOnly = true)
    @Override
    public TransporterResponse getTransporterById(UUID transporterId) {
        log.info("Fetching transporter with ID: {}", transporterId);
        Transporter transporter = transporterRepository.findById(transporterId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transporter not found with ID: " + transporterId));
        return EntityMapper.toResponse(transporter);
    }

    @Transactional
    @Override
    public TransporterResponse updateTrucks(UUID transporterId, UpdateTrucksRequest request) {
        log.info("Updating trucks for transporter: {}", transporterId);
        Transporter transporter = transporterRepository.findById(transporterId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transporter not found with ID: " + transporterId));

        transporter.setAvailableTrucks(request.getAvailableTrucks());
        Transporter updatedTransporter = transporterRepository.save(transporter);
        log.info("Trucks updated successfully for transporter: {}", transporterId);
        return EntityMapper.toResponse(updatedTransporter);
    }

    @Transactional
    @Override
    public void deductTrucks(UUID transporterId, String truckType, int count) {
        log.info("Deducting {} trucks of type {} from transporter: {}", count, truckType, transporterId);
        Transporter transporter = transporterRepository.findById(transporterId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transporter not found with ID: " + transporterId));

        List<AvailableTruck> trucks = transporter.getAvailableTrucks();
        Optional<AvailableTruck> truckOpt = trucks.stream()
                .filter(t -> t.getTruckType().equalsIgnoreCase(truckType))
                .findFirst();

        truckOpt.ifPresent(truck -> truck.setCount(truck.getCount() - count));

        transporterRepository.save(transporter);
        log.info("Trucks deducted successfully");
    }

    @Transactional
    @Override
    public void restoreTrucks(UUID transporterId, String truckType, int count) {
        log.info("Restoring {} trucks of type {} to transporter: {}", count, truckType, transporterId);
        Transporter transporter = transporterRepository.findById(transporterId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transporter not found with ID: " + transporterId));

        List<AvailableTruck> trucks = transporter.getAvailableTrucks();
        Optional<AvailableTruck> truckOpt = trucks.stream()
                .filter(t -> t.getTruckType().equalsIgnoreCase(truckType))
                .findFirst();

        if (truckOpt.isPresent()) {
            AvailableTruck truck = truckOpt.get();
            truck.setCount(truck.getCount() + count);
        } else {
            trucks.add(new AvailableTruck(truckType, count));
        }

        transporterRepository.save(transporter);
        log.info("Trucks restored successfully");
    }

    @Transactional(readOnly = true)
    @Override
    public int getAvailableTruckCount(UUID transporterId, String truckType) {
        Transporter transporter = transporterRepository.findById(transporterId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transporter not found with ID: " + transporterId));

        return transporter.getAvailableTrucks().stream()
                .filter(t -> t.getTruckType().equalsIgnoreCase(truckType))
                .findFirst()
                .map(AvailableTruck::getCount)
                .orElse(0);
    }
}

