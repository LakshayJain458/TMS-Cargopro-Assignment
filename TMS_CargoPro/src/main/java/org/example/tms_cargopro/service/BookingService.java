package org.example.tms_cargopro.service;

import org.example.tms_cargopro.dto.request.BookingRequest;
import org.example.tms_cargopro.dto.response.BookingResponse;

import java.util.UUID;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request);

    BookingResponse getBookingById(UUID bookingId);

    BookingResponse cancelBooking(UUID bookingId);
}

