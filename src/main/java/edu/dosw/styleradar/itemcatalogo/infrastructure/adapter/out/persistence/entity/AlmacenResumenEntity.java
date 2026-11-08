package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "almacen_resumen", schema = "itemcatalogo")
public class AlmacenResumenEntity {

    @Id
    @Column(name = "almacen_id")
    private UUID almacenId;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal reputacion;

    @Column(nullable = false)
    private boolean activo;

    protected AlmacenResumenEntity() {
    }

    public AlmacenResumenEntity(UUID almacenId, String nombre, BigDecimal reputacion, boolean activo) {
        this.almacenId = almacenId;
        this.nombre = nombre;
        this.reputacion = reputacion;
        this.activo = activo;
    }

    public UUID getAlmacenId() {
        return almacenId;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getReputacion() {
        return reputacion;
    }

    public boolean isActivo() {
        return activo;
    }
}
