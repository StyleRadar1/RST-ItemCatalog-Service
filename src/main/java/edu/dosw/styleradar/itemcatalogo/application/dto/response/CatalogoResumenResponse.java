package edu.dosw.styleradar.itemcatalogo.application.dto.response;

import java.util.List;
import java.util.UUID;

public record CatalogoResumenResponse(UUID almacenId, int totalPrendas, List<PrendaResponse> prendas) {
}
