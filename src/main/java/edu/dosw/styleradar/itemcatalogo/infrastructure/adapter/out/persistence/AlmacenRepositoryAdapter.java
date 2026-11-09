package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence;

import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.mapper.PrendaMapperOut;
import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.repository.AlmacenResumenJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AlmacenRepositoryAdapter implements AlmacenRepositoryPort {

    private final AlmacenResumenJpaRepository jpaRepository;

    public AlmacenRepositoryAdapter(AlmacenResumenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<AlmacenResumen> findById(UUID almacenId) {
        return jpaRepository.findById(almacenId).map(PrendaMapperOut::toDomain);
    }

    @Override
    public List<AlmacenResumen> findAll() {
        return jpaRepository.findAll().stream().map(PrendaMapperOut::toDomain).toList();
    }

    @Override
    public AlmacenResumen save(AlmacenResumen almacen) {
        return PrendaMapperOut.toDomain(jpaRepository.save(PrendaMapperOut.toEntity(almacen)));
    }
}
