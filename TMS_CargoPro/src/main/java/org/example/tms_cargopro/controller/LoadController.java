package org.example.tms_cargopro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tms_cargopro.dto.request.LoadRequest;
import org.example.tms_cargopro.dto.response.BestBidResponse;
import org.example.tms_cargopro.dto.response.LoadResponse;
import org.example.tms_cargopro.enums.LoadStatus;
import org.example.tms_cargopro.service.LoadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Load Management", description = "APIs for managing loads")
@RequiredArgsConstructor
@RequestMapping("/api/load")
@RestController
public class LoadController {
    private final LoadService loadService;

    @Operation(summary = "Create a new load", description = "Creates a new load posted by a shipper")
    @PostMapping
    public ResponseEntity<LoadResponse> createLoad(@Valid @RequestBody LoadRequest request) {
        LoadResponse response = loadService.createLoad(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get best bids for a load", description = "Returns bids sorted by score (1/rate * 0.7 + rating/5 * 0.3)")
    @GetMapping("/{loadId}/best-bids")
    public ResponseEntity<List<BestBidResponse>> getBestBids(@PathVariable UUID loadId) {
        List<BestBidResponse> response = loadService.getBestBids(loadId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cancel a load", description = "Cancels a load (cannot cancel if already booked)")
    @PatchMapping("/{loadId}/cancel")
    public ResponseEntity<LoadResponse> cancelLoad(@PathVariable UUID loadId) {
        LoadResponse response = loadService.cancelLoad(loadId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get load by ID", description = "Retrieves a specific load by its ID")
    @GetMapping("/{loadId}")
    public ResponseEntity<LoadResponse> getLoadById(@PathVariable UUID loadId) {
        LoadResponse response = loadService.getLoadById(loadId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get loads", description = "Get loads with optional filters")
    public ResponseEntity<Page<LoadResponse>> getLoads(

            @Parameter(description = "Shipper ID filter")
            @RequestParam(required = false)
            String shipperId,

            @Parameter(description = "Load status filter")
            @RequestParam(required = false)
            LoadStatus status,

            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10")
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<LoadResponse> response = loadService.getLoads(shipperId, status, pageable);
        return ResponseEntity.ok(response);
    }
}






