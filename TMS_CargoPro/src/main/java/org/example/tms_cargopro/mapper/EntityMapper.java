package org.example.tms_cargopro.mapper;

import org.example.tms_cargopro.dto.request.BidRequest;
import org.example.tms_cargopro.dto.request.LoadRequest;
import org.example.tms_cargopro.dto.request.TransporterRequest;
import org.example.tms_cargopro.dto.response.BidResponse;
import org.example.tms_cargopro.dto.response.BookingResponse;
import org.example.tms_cargopro.dto.response.LoadResponse;
import org.example.tms_cargopro.dto.response.TransporterResponse;
import org.example.tms_cargopro.entity.Bid;
import org.example.tms_cargopro.entity.Booking;
import org.example.tms_cargopro.entity.Load;
import org.example.tms_cargopro.entity.Transporter;

public class EntityMapper {

    public static Load toEntity(LoadRequest request) {
        Load load = new Load();
        load.setShipperId(request.getShipperId());
        load.setLoadingCity(request.getLoadingCity());
        load.setUnloadingCity(request.getUnloadingCity());
        load.setLoadingDate(request.getLoadingDate());
        load.setProductType(request.getProductType());
        load.setWeight(request.getWeight());
        load.setWeightUnit(request.getWeightUnit());
        load.setTruckType(request.getTruckType());
        load.setNoOfTrucks(request.getNoOfTrucks());
        return load;
    }

    public static LoadResponse toResponse(Load load) {
        return new LoadResponse(
                load.getLoadId(),
                load.getShipperId(),
                load.getLoadingCity(),
                load.getUnloadingCity(),
                load.getLoadingDate(),
                load.getProductType(),
                load.getWeight(),
                load.getWeightUnit(),
                load.getTruckType(),
                load.getNoOfTrucks(),
                load.getStatus(),
                load.getDatePosted(),
                load.getVersion()
        );
    }

    public static Transporter toEntity(TransporterRequest request) {
        Transporter transporter = new Transporter();
        transporter.setCompanyName(request.getCompanyName());
        transporter.setRating(request.getRating() != null ? request.getRating() : 3.0);
        transporter.setAvailableTrucks(request.getAvailableTrucks());
        return transporter;
    }

    public static TransporterResponse toResponse(Transporter transporter) {
        return new TransporterResponse(
                transporter.getTransporterId(),
                transporter.getCompanyName(),
                transporter.getRating(),
                transporter.getAvailableTrucks()
        );
    }

    public static Bid toEntity(BidRequest request) {
        Bid bid = new Bid();
        bid.setLoadId(request.getLoadId());
        bid.setTransporterId(request.getTransporterId());
        bid.setProposedRate(request.getProposedRate());
        bid.setTrucksOffered(request.getTrucksOffered());
        return bid;
    }

    public static BidResponse toResponse(Bid bid) {
        return new BidResponse(
                bid.getBidId(),
                bid.getLoadId(),
                bid.getTransporterId(),
                bid.getProposedRate(),
                bid.getTrucksOffered(),
                bid.getStatus(),
                bid.getSubmittedAt()
        );
    }

    public static BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getBookingId(),
                booking.getLoadId(),
                booking.getBidId(),
                booking.getTransporterId(),
                booking.getAllocatedTrucks(),
                booking.getFinalRate(),
                booking.getStatus(),
                booking.getBookedAt()
        );
    }
}

