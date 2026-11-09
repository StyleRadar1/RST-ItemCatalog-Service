package edu.dosw.styleradar.itemcatalogo.application.dto.response;

import java.util.List;
import java.util.UUID;

public record GrupoPendientesResponse(UUID almacenId, String almacen, int pendientes, List<PrendaResponse> prendas) {
}
