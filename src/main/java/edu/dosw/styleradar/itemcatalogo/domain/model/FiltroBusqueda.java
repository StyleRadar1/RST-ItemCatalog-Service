package edu.dosw.styleradar.itemcatalogo.domain.model;

import java.math.BigDecimal;

// Todos los campos son opcionales; la talla se agrega con la conexión a ms-inventario
public record FiltroBusqueda(String texto, String tipo, String color, String marca, String estilo,
                             BigDecimal precioMin, BigDecimal precioMax, Ordenamiento orden,
                             Ubicacion ubicacion) {
}
