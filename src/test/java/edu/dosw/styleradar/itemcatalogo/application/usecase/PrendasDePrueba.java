package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.AtributosBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.Fotografia;
import edu.dosw.styleradar.itemcatalogo.domain.model.Precio;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Datos de apoyo para las pruebas de casos de uso
final class PrendasDePrueba {

    private PrendasDePrueba() {
    }

    static AlmacenResumen almacen(String reputacion, boolean activo) {
        return new AlmacenResumen(UUID.randomUUID(), "Almacén", new BigDecimal(reputacion), activo);
    }

    static Prenda prenda(UUID almacenId, String tipo, EstadoPublicacion estado, LocalDateTime fecha) {
        return new Prenda(UUID.randomUUID(), almacenId, tipo + " de prueba", new AtributosBusqueda(tipo, "Azul", "Marca", "Urbano"),
                new Precio(new BigDecimal("100000")), List.of(new Fotografia("https://foto/1.jpg", 1)), List.of(),
                estado, true, fecha);
    }

    static Prenda aprobada(UUID almacenId, String tipo, LocalDateTime fecha) {
        return prenda(almacenId, tipo, EstadoPublicacion.APROBADA, fecha);
    }
}
