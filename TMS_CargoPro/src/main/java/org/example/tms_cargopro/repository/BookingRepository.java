package org.example.tms_cargopro.repository;

import org.example.tms_cargopro.entity.Booking;
import org.example.tms_cargopro.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByLoadId(UUID loadId);

    List<Booking> findByTransporterId(UUID transporterId);

    @Query("SELECT COALESCE(SUM(b.allocatedTrucks), 0) FROM Booking b " +
           "WHERE b.loadId = :loadId AND b.status <> :cancelledStatus")
    Integer getTotalAllocatedTrucksForLoad(@Param("loadId") UUID loadId,
                                           @Param("cancelledStatus") BookingStatus cancelledStatus);
}

