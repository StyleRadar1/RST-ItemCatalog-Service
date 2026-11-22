package edu.dosw.styleradar.itemcatalogo.domain.event;

import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;

import java.time.LocalDateTime;
import java.util.UUID;

public record ModeracionCambiadaEvent(UUID prendaId, UUID almacenId, String nombre, EstadoPublicacion estado,
                                      String motivo, LocalDateTime fecha, Long propietarioUsuarioId) {

    public static ModeracionCambiadaEvent de(Prenda prenda, String motivo) {
        return de(prenda, motivo, null);
    }

    public static ModeracionCambiadaEvent de(Prenda prenda, String motivo, Long propietarioUsuarioId) {
        return new ModeracionCambiadaEvent(prenda.getId(), prenda.getAlmacenId(), prenda.getNombre(),
                prenda.getEstado(), motivo, LocalDateTime.now(), propietarioUsuarioId);
    }
}
