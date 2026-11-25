package edu.dosw.styleradar.itemcatalogo.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record AlmacenResumen(UUID almacenId, String nombre, BigDecimal reputacion, boolean activo, Long administradorId,
                             String nit) {

    public AlmacenResumen(UUID almacenId, String nombre, BigDecimal reputacion, boolean activo) {
        this(almacenId, nombre, reputacion, activo, null, null);
    }

    public AlmacenResumen(UUID almacenId, String nombre, BigDecimal reputacion, boolean activo, Long administradorId) {
        this(almacenId, nombre, reputacion, activo, administradorId, null);
    }

    public AlmacenResumen suspender() {
        return new AlmacenResumen(almacenId, nombre, reputacion, false, administradorId, nit);
    }

    public AlmacenResumen conAdministrador(Long usuarioId) {
        return new AlmacenResumen(almacenId, nombre, reputacion, activo, usuarioId, nit);
    }
}
