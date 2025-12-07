package org.example.tms_cargopro.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms_cargopro.dto.request.BidRequest;
import org.example.tms_cargopro.dto.response.BidResponse;
import org.example.tms_cargopro.entity.Bid;
import org.example.tms_cargopro.entity.Load;
import org.example.tms_cargopro.enums.BidStatus;
import org.example.tms_cargopro.enums.LoadStatus;
import org.example.tms_cargopro.exception.InsufficientCapacityException;
import org.example.tms_cargopro.exception.InvalidStatusException;
import org.example.tms_cargopro.exception.ResourceNotFoundException;
import org.example.tms_cargopro.mapper.EntityMapper;
import org.example.tms_cargopro.repository.BidRepository;
import org.example.tms_cargopro.repository.LoadRepository;
import org.example.tms_cargopro.service.BidService;
import org.example.tms_cargopro.service.LoadService;
import org.example.tms_cargopro.service.TransporterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final LoadRepository loadRepository;
    private final TransporterService transporterService;
    private final LoadService loadService;

    @Transactional
    @Override
    public BidResponse createBid(BidRequest request) {
        log.info("Creating bid for load: {} by transporter: {}", request.getLoadId(), request.getTransporterId());

        Load load = loadRepository.findById(request.getLoadId())
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with ID: " + request.getLoadId()));

        if (load.getStatus() == LoadStatus.CANCELLED || load.getStatus() == LoadStatus.BOOKED) {
            throw new InvalidStatusException("Cannot bid on a load with status: " + load.getStatus());
        }

        transporterService.getTransporterById(request.getTransporterId());

        int availableTrucks = transporterService.getAvailableTruckCount(request.getTransporterId(), load.getTruckType());

        if (request.getTrucksOffered() > availableTrucks) {
            throw new InsufficientCapacityException(
                    String.format("Transporter has only %d trucks of type %s available, but offered %d",
                            availableTrucks, load.getTruckType(), request.getTrucksOffered()));
        }

        Bid bid = EntityMapper.toEntity(request);
        bid.setStatus(BidStatus.PENDING);
        Bid savedBid = bidRepository.save(bid);

        if (load.getStatus() == LoadStatus.POSTED) {
            loadService.updateLoadStatus(load.getLoadId(), LoadStatus.OPEN_FOR_BIDS);
        }

        log.info("Bid created with ID: {}", savedBid.getBidId());
        return EntityMapper.toResponse(savedBid);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BidResponse> getBids(UUID loadId, UUID transporterId, BidStatus status) {
        log.info("Fetching bids - loadId: {}, transporterId: {}, status: {}", loadId, transporterId, status);

        List<Bid> bids = new ArrayList<>();

        if (loadId != null && transporterId != null && status != null) {
            bids = bidRepository.findByLoadIdAndTransporterIdAndStatus(loadId, transporterId, status);
        } else if (loadId != null && transporterId != null) {
            bids = bidRepository.findByLoadIdAndTransporterId(loadId, transporterId);
        } else if (loadId != null && status != null) {
            bids = bidRepository.findByLoadIdAndStatus(loadId, status);
        } else if (transporterId != null && status != null) {
            bids = bidRepository.findByTransporterIdAndStatus(transporterId, status);
        } else if (loadId != null) {
            bids = bidRepository.findByLoadId(loadId);
        } else if (transporterId != null) {
            bids = bidRepository.findByTransporterId(transporterId);
        } else if (status != null) {
            bids = bidRepository.findByStatus(status);
        } else {
            bids = bidRepository.findAll();
        }

        return bids.stream()
                .map(EntityMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public BidResponse getBidById(UUID bidId) {
        log.info("Fetching bid with ID: {}", bidId);
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found with ID: " + bidId));
        return EntityMapper.toResponse(bid);
    }

    @Transactional
    @Override
    public BidResponse rejectBid(UUID bidId) {
        log.info("Rejecting bid with ID: {}", bidId);
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found with ID: " + bidId));

        if (bid.getStatus() != BidStatus.PENDING) {
            throw new InvalidStatusException("Can only reject bids with PENDING status");
        }

        bid.setStatus(BidStatus.REJECTED);
        Bid updatedBid = bidRepository.save(bid);
        log.info("Bid rejected successfully: {}", bidId);
        return EntityMapper.toResponse(updatedBid);
    }
}

