package org.example.tms_cargopro.repository;

import org.example.tms_cargopro.entity.Transporter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransporterRepository extends JpaRepository<Transporter, UUID> {
}

