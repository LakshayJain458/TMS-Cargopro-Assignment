package org.example.tms_cargopro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tms_cargopro.dto.request.TransporterRequest;
import org.example.tms_cargopro.dto.request.UpdateTrucksRequest;
import org.example.tms_cargopro.dto.response.TransporterResponse;
import org.example.tms_cargopro.service.TransporterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transporter")
@RequiredArgsConstructor
@Tag(name = "Transporter Management", description = "APIs for managing transporters")
public class TransporterController {

    private final TransporterService transporterService;

    @PostMapping
    @Operation(summary = "Create a new transporter", description = "Registers a new transporter")
    public ResponseEntity<TransporterResponse> createTransporter(
            @Valid @RequestBody TransporterRequest request) {
        TransporterResponse response = transporterService.createTransporter(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transporter by ID", description = "Retrieves a specific transporter by its ID")
    public ResponseEntity<TransporterResponse> getTransporterById(@PathVariable UUID id) {
        TransporterResponse response = transporterService.getTransporterById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/trucks")
    @Operation(summary = "Update transporter trucks", description = "Updates the available trucks for a transporter")
    public ResponseEntity<TransporterResponse> updateTrucks(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTrucksRequest request) {
        TransporterResponse response = transporterService.updateTrucks(id, request);
        return ResponseEntity.ok(response);
    }
}

