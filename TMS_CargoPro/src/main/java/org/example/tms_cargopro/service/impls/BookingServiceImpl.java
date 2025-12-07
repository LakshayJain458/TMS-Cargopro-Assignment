package org.example.tms_cargopro.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms_cargopro.dto.request.BookingRequest;
import org.example.tms_cargopro.dto.response.BookingResponse;
import org.example.tms_cargopro.entity.Bid;
import org.example.tms_cargopro.entity.Booking;
import org.example.tms_cargopro.entity.Load;
import org.example.tms_cargopro.enums.BidStatus;
import org.example.tms_cargopro.enums.BookingStatus;
import org.example.tms_cargopro.enums.LoadStatus;
import org.example.tms_cargopro.exception.InvalidStatusException;
import org.example.tms_cargopro.exception.LoadAlreadyBookedException;
import org.example.tms_cargopro.exception.ResourceNotFoundException;
import org.example.tms_cargopro.mapper.EntityMapper;
import org.example.tms_cargopro.repository.BidRepository;
import org.example.tms_cargopro.repository.BookingRepository;
import org.example.tms_cargopro.repository.LoadRepository;
import org.example.tms_cargopro.service.BookingService;
import org.example.tms_cargopro.service.LoadService;
import org.example.tms_cargopro.service.TransporterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BidRepository bidRepository;
    private final LoadRepository loadRepository;
    private final TransporterService transporterService;
    private final LoadService loadService;

    @Transactional
    @Override
    public BookingResponse createBooking(BookingRequest request) {
        log.info("Creating booking for bid: {}", request.getBidId());

        Bid bid = bidRepository.findById(request.getBidId())
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found with ID: " + request.getBidId()));

        if (bid.getStatus() != BidStatus.PENDING) {
            throw new InvalidStatusException("Can only accept bids with PENDING status");
        }

        Load load = loadRepository.findByLoadId(bid.getLoadId())
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with ID: " + bid.getLoadId()));

        if (load.getStatus() == LoadStatus.CANCELLED) {
            throw new InvalidStatusException("Cannot book a cancelled load");
        }
        if (load.getStatus() == LoadStatus.BOOKED) {
            throw new LoadAlreadyBookedException("Load is already fully booked");
        }

        Integer allocatedTrucks = bookingRepository.getTotalAllocatedTrucksForLoad(load.getLoadId(), BookingStatus.CANCELLED);
        int remainingTrucks = load.getNoOfTrucks() - allocatedTrucks;

        if (bid.getTrucksOffered() > remainingTrucks) {
            throw new InvalidStatusException(String.format("Only %d trucks remaining for this load, but bid offered %d", remainingTrucks, bid.getTrucksOffered()));
        }

        bid.setStatus(BidStatus.ACCEPTED);
        bidRepository.save(bid);

        Booking booking = new Booking();
        booking.setLoadId(load.getLoadId());
        booking.setBidId(bid.getBidId());
        booking.setTransporterId(bid.getTransporterId());
        booking.setAllocatedTrucks(bid.getTrucksOffered());
        booking.setFinalRate(bid.getProposedRate());
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);

        transporterService.deductTrucks(bid.getTransporterId(), load.getTruckType(), bid.getTrucksOffered());

        int newRemainingTrucks = remainingTrucks - bid.getTrucksOffered();
        if (newRemainingTrucks == 0) {
            loadService.updateLoadStatus(load.getLoadId(), LoadStatus.BOOKED);
        }

        log.info("Booking created with ID: {}", savedBooking.getBookingId());
        return EntityMapper.toResponse(savedBooking);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingResponse getBookingById(UUID bookingId) {
        log.info("Fetching booking with ID: {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));
        return EntityMapper.toResponse(booking);
    }

    @Transactional
    @Override
    public BookingResponse cancelBooking(UUID bookingId) {
        log.info("Cancelling booking with ID: {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new InvalidStatusException("Can only cancel bookings with CONFIRMED status");
        }

        Load load = loadRepository.findById(booking.getLoadId())
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with ID: " + booking.getLoadId()));

        booking.setStatus(BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);

        transporterService.restoreTrucks(booking.getTransporterId(), load.getTruckType(), booking.getAllocatedTrucks());

        if (load.getStatus() == LoadStatus.BOOKED) {
            loadService.updateLoadStatus(load.getLoadId(), LoadStatus.OPEN_FOR_BIDS);
        }

        log.info("Booking cancelled successfully: {}", bookingId);
        return EntityMapper.toResponse(updatedBooking);
    }
}

