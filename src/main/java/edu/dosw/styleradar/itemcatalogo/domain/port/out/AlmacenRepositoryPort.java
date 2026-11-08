package edu.dosw.styleradar.itemcatalogo.domain.port.out;

import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlmacenRepositoryPort {

    Optional<AlmacenResumen> findById(UUID almacenId);

    List<AlmacenResumen> findAll();
}
