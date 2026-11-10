package edu.dosw.styleradar.itemcatalogo.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

// Se publica al retirar una prenda; lo consume ms-playlist
public record PrendaEliminadaEvent(UUID prendaId, UUID almacenId, LocalDateTime fecha) {
}
