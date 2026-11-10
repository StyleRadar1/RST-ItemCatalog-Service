package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity;

import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "moderacion_publicacion", schema = "itemcatalogo")
public class ModeracionEntity {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "prenda_id", nullable = false)
    private PrendaEntity prenda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPublicacion decision;

    @Column(length = 300)
    private String motivo;

    @Column(name = "moderador_id", nullable = false)
    private UUID moderadorId;

    @Column(nullable = false)
    private LocalDateTime fecha;

    protected ModeracionEntity() {
    }

    public ModeracionEntity(UUID id, PrendaEntity prenda, EstadoPublicacion decision, String motivo, UUID moderadorId,
                            LocalDateTime fecha) {
        this.id = id;
        this.prenda = prenda;
        this.decision = decision;
        this.motivo = motivo;
        this.moderadorId = moderadorId;
        this.fecha = fecha;
    }

    public UUID getId() {
        return id;
    }

    public EstadoPublicacion getDecision() {
        return decision;
    }

    public String getMotivo() {
        return motivo;
    }

    public UUID getModeradorId() {
        return moderadorId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
