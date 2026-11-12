package edu.dosw.styleradar.itemcatalogo.domain.event;

import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;

import java.time.LocalDateTime;
import java.util.UUID;

// Se publica cada vez que un administrador aprueba o rechaza una prenda
public record ModeracionCambiadaEvent(UUID prendaId, UUID almacenId, String nombre, EstadoPublicacion estado,
                                      String motivo, LocalDateTime fecha) {

    public static ModeracionCambiadaEvent de(Prenda prenda, String motivo) {
        return new ModeracionCambiadaEvent(prenda.getId(), prenda.getAlmacenId(), prenda.getNombre(),
                prenda.getEstado(), motivo, LocalDateTime.now());
    }
}
