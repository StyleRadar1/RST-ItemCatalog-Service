package edu.dosw.styleradar.itemcatalogo.domain.port.out;

import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;

import java.util.Optional;
import java.util.UUID;

public interface StockPrendaPort {

    Optional<Disponibilidad> disponibilidad(UUID prendaId);
}
