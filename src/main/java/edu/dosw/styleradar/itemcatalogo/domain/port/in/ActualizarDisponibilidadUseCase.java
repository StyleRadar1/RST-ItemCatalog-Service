package edu.dosw.styleradar.itemcatalogo.domain.port.in;

import java.util.UUID;

public interface ActualizarDisponibilidadUseCase {

    void marcarAgotada(UUID prendaId);

    void suspenderAlmacen(UUID almacenId);
}
