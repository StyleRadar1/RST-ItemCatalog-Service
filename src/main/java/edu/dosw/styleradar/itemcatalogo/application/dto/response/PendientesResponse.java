package edu.dosw.styleradar.itemcatalogo.application.dto.response;

import java.util.List;

public record PendientesResponse(String tipoPublicador, int total, List<GrupoPendientesResponse> grupos) {
}
