package edu.dosw.styleradar.itemcatalogo.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record AlmacenResumen(UUID almacenId, String nombre, BigDecimal reputacion, boolean activo) {
}
