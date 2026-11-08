package edu.dosw.styleradar.itemcatalogo.domain.port.out;

import edu.dosw.styleradar.itemcatalogo.domain.model.Ubicacion;

import java.util.UUID;

public interface DistanciaAlmacenPort {

    double kilometros(UUID almacenId, Ubicacion ubicacion);
}
