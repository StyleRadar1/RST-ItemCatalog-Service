package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence;

import edu.dosw.styleradar.itemcatalogo.application.mapper.out.PrendaMapperOut;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.repository.PrendaJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PrendaRepositoryAdapter implements PrendaRepositoryPort {

    private final PrendaJpaRepository jpaRepository;

    public PrendaRepositoryAdapter(PrendaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Prenda save(Prenda prenda) {
        return PrendaMapperOut.toDomain(jpaRepository.save(PrendaMapperOut.toEntity(prenda)));
    }

    @Override
    public Optional<Prenda> findById(UUID id) {
        return jpaRepository.findById(id).map(PrendaMapperOut::toDomain);
    }

    @Override
    public List<Prenda> findByEstado(EstadoPublicacion estado) {
        return jpaRepository.findByEstado(estado).stream().map(PrendaMapperOut::toDomain).toList();
    }

    @Override
    public List<Prenda> findByAlmacenId(UUID almacenId) {
        return jpaRepository.findByAlmacenId(almacenId).stream().map(PrendaMapperOut::toDomain).toList();
    }
}
