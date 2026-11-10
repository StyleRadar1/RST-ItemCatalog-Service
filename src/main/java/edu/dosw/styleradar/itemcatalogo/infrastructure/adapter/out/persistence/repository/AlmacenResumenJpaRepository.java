package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.repository;

import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity.AlmacenResumenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlmacenResumenJpaRepository extends JpaRepository<AlmacenResumenEntity, UUID> {
}
