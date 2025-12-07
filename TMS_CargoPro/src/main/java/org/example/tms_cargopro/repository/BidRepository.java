package org.example.tms_cargopro.repository;

import org.example.tms_cargopro.entity.Bid;
import org.example.tms_cargopro.enums.BidStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {

    List<Bid> findByLoadIdAndStatus(UUID loadId, BidStatus status);

    List<Bid> findByLoadId(UUID loadId);

    List<Bid> findByTransporterId(UUID transporterId);

    List<Bid> findByTransporterIdAndStatus(UUID transporterId, BidStatus status);

    @Query("SELECT b FROM Bid b WHERE b.loadId = :loadId AND b.transporterId = :transporterId")
    List<Bid> findByLoadIdAndTransporterId(@Param("loadId") UUID loadId,
                                            @Param("transporterId") UUID transporterId);

    @Query("SELECT b FROM Bid b WHERE b.loadId = :loadId AND b.transporterId = :transporterId AND b.status = :status")
    List<Bid> findByLoadIdAndTransporterIdAndStatus(@Param("loadId") UUID loadId,
                                                      @Param("transporterId") UUID transporterId,
                                                      @Param("status") BidStatus status);

    boolean existsByLoadIdAndStatus(UUID loadId, BidStatus status);

    List<Bid> findByStatus(BidStatus status);
}

