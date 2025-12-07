package org.example.tms_cargopro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms_cargopro.enums.BookingStatus;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private UUID bookingId;
    private UUID loadId;
    private UUID bidId;
    private UUID transporterId;
    private Integer allocatedTrucks;
    private Double finalRate;
    private BookingStatus status;
    private Instant bookedAt;
}

