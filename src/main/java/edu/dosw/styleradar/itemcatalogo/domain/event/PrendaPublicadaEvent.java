package edu.dosw.styleradar.itemcatalogo.domain.event;

import edu.dosw.styleradar.itemcatalogo.domain.model.Fotografia;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public record PrendaPublicadaEvent(UUID prendaId, UUID almacenId, String nombre, String tipo, String color,
                                   String marca, String estilo, BigDecimal precio, String imagenUrl,
                                   LocalDateTime fecha, List<String> tallas) {

    public static PrendaPublicadaEvent de(Prenda prenda) {
        return de(prenda, null);
    }

    public static PrendaPublicadaEvent de(Prenda prenda, List<String> tallas) {
        return new PrendaPublicadaEvent(prenda.getId(), prenda.getAlmacenId(), prenda.getNombre(),
                prenda.getAtributos().tipo(), prenda.getAtributos().color(), prenda.getAtributos().marca(),
                prenda.getAtributos().estilo(), prenda.getPrecio().valor(), primeraFoto(prenda), prenda.getFechaPublicacion(),
                tallas);
    }

    private static String primeraFoto(Prenda prenda) {
        return prenda.getFotografias().stream()
                .min(Comparator.comparingInt(Fotografia::orden))
                .map(Fotografia::url)
                .orElse(null);
    }
}
