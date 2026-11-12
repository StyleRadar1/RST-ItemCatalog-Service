package edu.dosw.styleradar.itemcatalogo.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

// Se publica al retirar una prenda del catálogo
public record PrendaRetiradaEvent(UUID prendaId, UUID almacenId, String motivo, LocalDateTime fecha) {
}
