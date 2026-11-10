package edu.dosw.styleradar.itemcatalogo.domain.model;

import java.math.BigDecimal;

public record FiltroBusqueda(String texto, String tipo, String color, String marca, String estilo, String talla,
                             BigDecimal precioMin, BigDecimal precioMax, Ordenamiento orden,
                             Ubicacion ubicacion) {
}
