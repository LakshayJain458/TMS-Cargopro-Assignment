package org.example.tms_cargopro.service;

import org.example.tms_cargopro.dto.request.BidRequest;
import org.example.tms_cargopro.dto.response.BidResponse;
import org.example.tms_cargopro.enums.BidStatus;
import java.util.List;
import java.util.UUID;

public interface BidService {

    BidResponse rejectBid(UUID bidId);

    BidResponse getBidById(UUID bidId);

    List<BidResponse> getBids(UUID loadId, UUID transporterId, BidStatus status);

    BidResponse createBid(BidRequest request);
}



