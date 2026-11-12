package edu.dosw.styleradar.itemcatalogo.domain.event;

import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// Se publica cuando una prenda queda aprobada y visible en el catálogo
public record PrendaPublicadaEvent(UUID prendaId, UUID almacenId, String nombre, String tipo, String color,
                                   String marca, String estilo, BigDecimal precio, LocalDateTime fecha) {

    public static PrendaPublicadaEvent de(Prenda prenda) {
        return new PrendaPublicadaEvent(prenda.getId(), prenda.getAlmacenId(), prenda.getNombre(),
                prenda.getAtributos().tipo(), prenda.getAtributos().color(), prenda.getAtributos().marca(),
                prenda.getAtributos().estilo(), prenda.getPrecio().valor(), prenda.getFechaPublicacion());
    }
}
