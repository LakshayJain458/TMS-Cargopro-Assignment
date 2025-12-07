package org.example.tms_cargopro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tms_cargopro.dto.request.BidRequest;
import org.example.tms_cargopro.dto.response.BidResponse;
import org.example.tms_cargopro.enums.BidStatus;
import org.example.tms_cargopro.service.BidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bid")
@RequiredArgsConstructor
@Tag(name = "Bid Management", description = "APIs for managing bids")
public class BidController {

    private final BidService bidService;

    @PostMapping
    @Operation(summary = "Create a new bid", description = "Submits a new bid for a load")
    public ResponseEntity<BidResponse> createBid(@Valid @RequestBody BidRequest request) {
        BidResponse response = bidService.createBid(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get bids", description = "Get bids with optional filters")
    public ResponseEntity<List<BidResponse>> getBids(
            @Parameter(description = "Load ID filter")
            @RequestParam(required = false) UUID loadId,

            @Parameter(description = "Transporter ID filter")
            @RequestParam(required = false) UUID transporterId,

            @Parameter(description = "Bid status filter")
            @RequestParam(required = false) BidStatus status) {
        List<BidResponse> response = bidService.getBids(loadId, transporterId, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{bidId}")
    @Operation(summary = "Get bid by ID", description = "Retrieves a specific bid by its ID")
    public ResponseEntity<BidResponse> getBidById(@PathVariable UUID bidId) {
        BidResponse response = bidService.getBidById(bidId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{bidId}/reject")
    @Operation(summary = "Reject a bid", description = "Rejects a pending bid")
    public ResponseEntity<BidResponse> rejectBid(@PathVariable UUID bidId) {
        BidResponse response = bidService.rejectBid(bidId);
        return ResponseEntity.ok(response);
    }
}

