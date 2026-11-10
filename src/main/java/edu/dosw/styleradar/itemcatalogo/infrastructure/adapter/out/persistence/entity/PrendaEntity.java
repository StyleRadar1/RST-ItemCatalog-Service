package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "prenda", schema = "itemcatalogo")
public class PrendaEntity {

    @Id
    private UUID id;

    @Column(name = "almacen_id", nullable = false)
    private UUID almacenId;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 60)
    private String tipo;

    @Column(length = 40)
    private String color;

    @Column(length = 60)
    private String marca;

    @Column(length = 60)
    private String estilo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPublicacion estado;

    @Column(nullable = false)
    private boolean disponible;

    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDateTime fechaPublicacion;

    // SUBSELECT evita duplicados al cargar las dos listas
    @OneToMany(mappedBy = "prenda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("orden")
    private List<FotografiaEntity> fotografias = new ArrayList<>();

    @OneToMany(mappedBy = "prenda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("fecha")
    private List<ModeracionEntity> moderaciones = new ArrayList<>();

    protected PrendaEntity() {
    }

    public PrendaEntity(UUID id, UUID almacenId, String nombre, String tipo, String color, String marca, String estilo,
                        BigDecimal precio, EstadoPublicacion estado, boolean disponible, LocalDateTime fechaPublicacion) {
        this.id = id;
        this.almacenId = almacenId;
        this.nombre = nombre;
        this.tipo = tipo;
        this.color = color;
        this.marca = marca;
        this.estilo = estilo;
        this.precio = precio;
        this.estado = estado;
        this.disponible = disponible;
        this.fechaPublicacion = fechaPublicacion;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAlmacenId() {
        return almacenId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getColor() {
        return color;
    }

    public String getMarca() {
        return marca;
    }

    public String getEstilo() {
        return estilo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public EstadoPublicacion getEstado() {
        return estado;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public List<FotografiaEntity> getFotografias() {
        return fotografias;
    }

    public List<ModeracionEntity> getModeraciones() {
        return moderaciones;
    }
}
