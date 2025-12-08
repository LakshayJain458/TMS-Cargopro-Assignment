package org.example.tms_cargopro.repository;

import jakarta.persistence.LockModeType;
import org.example.tms_cargopro.entity.Load;
import org.example.tms_cargopro.enums.LoadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoadRepository extends JpaRepository<Load, UUID> {

    @Query("SELECT l FROM Load l WHERE l.loadId = :loadId")
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Load> findByLoadId(@Param("loadId") UUID loadId);

    Page<Load> findByStatus(LoadStatus status, Pageable pageable);

    Page<Load> findByShipperId(String shipperId, Pageable pageable);

    Page<Load> findByShipperIdAndStatus(String shipperId, LoadStatus status, Pageable pageable);
}