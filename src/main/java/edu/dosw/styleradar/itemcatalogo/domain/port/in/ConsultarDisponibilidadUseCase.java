package edu.dosw.styleradar.itemcatalogo.domain.port.in;

import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsultarDisponibilidadUseCase {

    Disponibilidad consultar(UUID prendaId);

    Optional<List<String>> tallasOfertadas(UUID prendaId);
}
