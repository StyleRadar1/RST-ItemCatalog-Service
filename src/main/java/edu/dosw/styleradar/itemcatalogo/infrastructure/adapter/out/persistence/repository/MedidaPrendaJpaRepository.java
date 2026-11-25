package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.repository;

import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity.MedidaPrendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MedidaPrendaJpaRepository extends JpaRepository<MedidaPrendaEntity, UUID> {

    List<MedidaPrendaEntity> findByPrendaIdOrderByTalla(UUID prendaId);

    @Modifying
    @Query("delete from MedidaPrendaEntity m where m.prendaId = :prendaId")
    void eliminarDePrenda(@Param("prendaId") UUID prendaId);
}
