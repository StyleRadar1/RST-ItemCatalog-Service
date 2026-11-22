package edu.dosw.styleradar.itemcatalogo.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record AlmacenResumen(UUID almacenId, String nombre, BigDecimal reputacion, boolean activo, Long administradorId) {

    public AlmacenResumen(UUID almacenId, String nombre, BigDecimal reputacion, boolean activo) {
        this(almacenId, nombre, reputacion, activo, null);
    }

    public AlmacenResumen suspender() {
        return new AlmacenResumen(almacenId, nombre, reputacion, false, administradorId);
    }

    public AlmacenResumen conAdministrador(Long usuarioId) {
        return new AlmacenResumen(almacenId, nombre, reputacion, activo, usuarioId);
    }
}
