package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "medida_prenda", schema = "itemcatalogo")
public class MedidaPrendaEntity {

    @Id
    private UUID id;

    @Column(name = "prenda_id", nullable = false)
    private UUID prendaId;

    @Column(nullable = false, length = 10)
    private String talla;

    @Column(name = "min_pecho")
    private Double minPecho;

    @Column(name = "max_pecho")
    private Double maxPecho;

    @Column(name = "min_cintura")
    private Double minCintura;

    @Column(name = "max_cintura")
    private Double maxCintura;

    @Column(name = "min_cadera")
    private Double minCadera;

    @Column(name = "max_cadera")
    private Double maxCadera;

    private Double largo;

    protected MedidaPrendaEntity() {
    }

    public MedidaPrendaEntity(UUID id, UUID prendaId, String talla, Double minPecho, Double maxPecho, Double minCintura,
                              Double maxCintura, Double minCadera, Double maxCadera, Double largo) {
        this.id = id;
        this.prendaId = prendaId;
        this.talla = talla;
        this.minPecho = minPecho;
        this.maxPecho = maxPecho;
        this.minCintura = minCintura;
        this.maxCintura = maxCintura;
        this.minCadera = minCadera;
        this.maxCadera = maxCadera;
        this.largo = largo;
    }

    public UUID getPrendaId() {
        return prendaId;
    }

    public String getTalla() {
        return talla;
    }

    public Double getMinPecho() {
        return minPecho;
    }

    public Double getMaxPecho() {
        return maxPecho;
    }

    public Double getMinCintura() {
        return minCintura;
    }

    public Double getMaxCintura() {
        return maxCintura;
    }

    public Double getMinCadera() {
        return minCadera;
    }

    public Double getMaxCadera() {
        return maxCadera;
    }

    public Double getLargo() {
        return largo;
    }
}
