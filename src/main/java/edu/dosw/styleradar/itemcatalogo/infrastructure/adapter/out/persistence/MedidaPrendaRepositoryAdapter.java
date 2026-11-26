package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence;

import edu.dosw.styleradar.itemcatalogo.domain.model.MedidaPrenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.MedidaPrendaRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity.MedidaPrendaEntity;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.repository.MedidaPrendaJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class MedidaPrendaRepositoryAdapter implements MedidaPrendaRepositoryPort {

    private final MedidaPrendaJpaRepository jpaRepository;

    public MedidaPrendaRepositoryAdapter(MedidaPrendaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<MedidaPrenda> findByPrendaId(UUID prendaId) {
        return jpaRepository.findByPrendaIdOrderByTalla(prendaId).stream().map(MedidaPrendaRepositoryAdapter::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public List<MedidaPrenda> reemplazar(UUID prendaId, List<MedidaPrenda> medidas) {
        jpaRepository.eliminarDePrenda(prendaId);
        jpaRepository.flush();
        return jpaRepository.saveAll(medidas.stream().map(m -> toEntity(prendaId, m)).toList()).stream()
                .map(MedidaPrendaRepositoryAdapter::toDomain).toList();
    }

    static MedidaPrenda toDomain(MedidaPrendaEntity e) {
        return new MedidaPrenda(e.getTalla(), e.getMinPecho(), e.getMaxPecho(), e.getMinCintura(), e.getMaxCintura(),
                e.getMinCadera(), e.getMaxCadera(), e.getLargo());
    }

    static MedidaPrendaEntity toEntity(UUID prendaId, MedidaPrenda m) {
        return new MedidaPrendaEntity(UUID.randomUUID(), prendaId, m.talla(), m.minPecho(), m.maxPecho(), m.minCintura(),
                m.maxCintura(), m.minCadera(), m.maxCadera(), m.largo());
    }
}
