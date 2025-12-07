package org.example.tms_cargopro.service;

import org.example.tms_cargopro.dto.request.LoadRequest;
import org.example.tms_cargopro.dto.response.BestBidResponse;
import org.example.tms_cargopro.dto.response.LoadResponse;
import org.example.tms_cargopro.enums.LoadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface LoadService {

    LoadResponse createLoad(LoadRequest request);

    Page<LoadResponse> getLoads(String shipperId, LoadStatus status, Pageable pageable);

    LoadResponse getLoadById(UUID loadId);

    LoadResponse cancelLoad(UUID loadId);

    void updateLoadStatus(UUID loadId, LoadStatus newStatus);

    List<BestBidResponse> getBestBids(UUID loadId);
}

