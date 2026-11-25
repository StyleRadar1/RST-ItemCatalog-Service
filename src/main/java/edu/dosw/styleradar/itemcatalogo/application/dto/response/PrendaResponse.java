package edu.dosw.styleradar.itemcatalogo.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PrendaResponse(UUID id, UUID almacenId, String nombre, String tipo, String color, String marca,
                             String estilo, BigDecimal precio, List<String> fotografias, String estado,
                             boolean disponible, LocalDateTime fechaPublicacion, Double distanciaKm,
                             @Schema(description = "Store NIT in ms-tiendas, used to open the chat with the store "
                                     + "(POST /api/v1/stores/{nit}/conversations)", example = "900555777-1",
                                     nullable = true) String storeNit) {
}
