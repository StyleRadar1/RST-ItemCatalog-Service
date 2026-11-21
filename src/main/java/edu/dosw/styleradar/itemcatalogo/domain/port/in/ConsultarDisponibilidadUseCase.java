package edu.dosw.styleradar.itemcatalogo.domain.port.in;

import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;

import java.util.UUID;

public interface ConsultarDisponibilidadUseCase {

    Disponibilidad consultar(UUID prendaId);
}
