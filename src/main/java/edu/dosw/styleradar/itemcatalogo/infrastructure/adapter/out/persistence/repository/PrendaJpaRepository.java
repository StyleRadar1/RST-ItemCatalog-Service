package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.repository;

import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity.PrendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrendaJpaRepository extends JpaRepository<PrendaEntity, UUID> {

    List<PrendaEntity> findByEstado(EstadoPublicacion estado);

    List<PrendaEntity> findByAlmacenId(UUID almacenId);
}
