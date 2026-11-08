package edu.dosw.styleradar.itemcatalogo.domain.port.out;

import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrendaRepositoryPort {

    Prenda save(Prenda prenda);

    Optional<Prenda> findById(UUID id);

    List<Prenda> findByEstado(EstadoPublicacion estado);

    List<Prenda> findByAlmacenId(UUID almacenId);
}
