package edu.dosw.styleradar.itemcatalogo.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record NuevaPrendaCreadaEvent(UUID prendaId, UUID almacenId, LocalDateTime fecha) {
}
