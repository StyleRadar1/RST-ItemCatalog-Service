package edu.dosw.styleradar.itemcatalogo.domain.port.in;

import java.util.UUID;

// Reacciona a los eventos de inventario y de pagos
public interface ActualizarDisponibilidadUseCase {

    void marcarAgotada(UUID prendaId);

    void suspenderAlmacen(UUID almacenId);
}
