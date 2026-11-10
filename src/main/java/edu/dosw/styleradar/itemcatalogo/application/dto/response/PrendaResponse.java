package edu.dosw.styleradar.itemcatalogo.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PrendaResponse(UUID id, UUID almacenId, String nombre, String tipo, String color, String marca,
                             String estilo, BigDecimal precio, List<String> fotografias, String estado,
                             boolean disponible, LocalDateTime fechaPublicacion) {
}
