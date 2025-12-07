package org.example.tms_cargopro.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms_cargopro.dto.request.LoadRequest;
import org.example.tms_cargopro.dto.response.BestBidResponse;
import org.example.tms_cargopro.dto.response.LoadResponse;
import org.example.tms_cargopro.entity.Bid;
import org.example.tms_cargopro.entity.Load;
import org.example.tms_cargopro.entity.Transporter;
import org.example.tms_cargopro.enums.BidStatus;
import org.example.tms_cargopro.enums.LoadStatus;
import org.example.tms_cargopro.exception.InvalidStatusException;
import org.example.tms_cargopro.exception.ResourceNotFoundException;
import org.example.tms_cargopro.mapper.EntityMapper;
import org.example.tms_cargopro.repository.BidRepository;
import org.example.tms_cargopro.repository.LoadRepository;
import org.example.tms_cargopro.repository.TransporterRepository;
import org.example.tms_cargopro.service.LoadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadServiceImpl implements LoadService {

    private final TransporterRepository transporterRepository;
    private final BidRepository bidRepository;
    private final LoadRepository loadRepository;

    @Transactional
    @Override
    public LoadResponse createLoad(LoadRequest request) {
        log.info("Creating new load for shipper: {}", request.getShipperId());
        Load load = EntityMapper.toEntity(request);
        load.setStatus(LoadStatus.POSTED);
        Load savedLoad = loadRepository.save(load);
        log.info("Load created with ID: {}", savedLoad.getLoadId());
        return EntityMapper.toResponse(savedLoad);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<LoadResponse> getLoads(String shipperId, LoadStatus status, Pageable pageable) {
        log.info("Fetching loads - shipperId: {}, status: {}", shipperId, status);

        Page<Load> loads;
        if (shipperId != null && status != null) {
            loads = loadRepository.findByShipperIdAndStatus(shipperId, status, pageable);
        } else if (shipperId != null) {
            loads = loadRepository.findByShipperId(shipperId, pageable);
        } else if (status != null) {
            loads = loadRepository.findByStatus(status, pageable);
        } else {
            loads = loadRepository.findAll(pageable);
        }

        return loads.map(EntityMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public LoadResponse getLoadById(UUID loadId) {
        log.info("Fetching load with ID: {}", loadId);
        Load load = loadRepository.findById(loadId)
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with ID: " + loadId));
        return EntityMapper.toResponse(load);
    }

    @Transactional
    @Override
    public LoadResponse cancelLoad(UUID loadId) {
        log.info("Cancelling load with ID: {}", loadId);
        Load load = loadRepository.findById(loadId)
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with ID: " + loadId));

        if (load.getStatus() == LoadStatus.BOOKED) {
            log.warn("Attempt to cancel a BOOKED load: {}", loadId);
            throw new InvalidStatusException("Cannot cancel a load that is already booked");
        }

        load.setStatus(LoadStatus.CANCELLED);
        Load updatedLoad = loadRepository.save(load);
        log.info("Load cancelled successfully: {}", loadId);
        return EntityMapper.toResponse(updatedLoad);
    }

    @Transactional
    @Override
    public void updateLoadStatus(UUID loadId, LoadStatus newStatus) {
        log.info("Updating load status - ID: {}, New Status: {}", loadId, newStatus);
        Load load = loadRepository.findById(loadId)
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with ID: " + loadId));
        load.setStatus(newStatus);
        loadRepository.save(load);
        log.info("Load status updated - ID: {}, Status: {}", loadId, newStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BestBidResponse> getBestBids(UUID loadId) {
        log.info("Fetching best bids for load: {}", loadId);

        loadRepository.findById(loadId)
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with ID: " + loadId));

        List<Bid> bids = bidRepository.findByLoadIdAndStatus(loadId, BidStatus.PENDING);
        if (bids.isEmpty()) {
            log.info("No pending bids found for load: {}", loadId);
            return List.of();
        }

        List<BestBidResponse> bestBids = bids.stream()
                .map(bid -> {
                    Transporter transporter = transporterRepository.findById(bid.getTransporterId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Transporter not found with ID: " + bid.getTransporterId()));

                    double proposedRate = bid.getProposedRate() <= 0 ? 1.0 : bid.getProposedRate();

                    double score = (1.0 / proposedRate) * 0.7 + (transporter.getRating() / 5.0) * 0.3;
                    score = Math.round(score * 100.0) / 100.0;
                    return new BestBidResponse(
                            bid.getBidId(),
                            bid.getLoadId(),
                            bid.getTransporterId(),
                            transporter.getCompanyName(),
                            bid.getProposedRate(),
                            bid.getTrucksOffered(),
                            transporter.getRating(),
                            score
                    );
                })
                .sorted(Comparator.comparingDouble(BestBidResponse::getScore).reversed())
                .toList();

        log.info("Found {} best bids for load: {}", bestBids.size(), loadId);
        return bestBids;
    }
}

