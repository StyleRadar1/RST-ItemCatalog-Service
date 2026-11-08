package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "fotografia_prenda", schema = "itemcatalogo")
public class FotografiaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "prenda_id", nullable = false)
    private PrendaEntity prenda;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false)
    private int orden;

    protected FotografiaEntity() {
    }

    public FotografiaEntity(PrendaEntity prenda, String url, int orden) {
        this.prenda = prenda;
        this.url = url;
        this.orden = orden;
    }

    public String getUrl() {
        return url;
    }

    public int getOrden() {
        return orden;
    }
}
