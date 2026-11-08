package edu.dosw.styleradar.itemcatalogo.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Moderacion(UUID id, EstadoPublicacion decision, String motivo, UUID moderadorId, LocalDateTime fecha) {
}
