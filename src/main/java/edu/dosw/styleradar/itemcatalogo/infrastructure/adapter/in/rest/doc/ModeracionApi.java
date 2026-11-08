package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.ModeracionRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Moderación", description = "Revisión de publicaciones por el administrador")
public interface ModeracionApi {

    @Operation(summary = "Revisar publicaciones pendientes", description = "Prendas pendientes agrupadas por almacén (SR-RF-87).")
    @ApiResponse(responseCode = "200", description = "Pendientes por almacén")
    ResponseEntity<Map<UUID, List<PrendaResponse>>> pendientes();

    @Operation(summary = "Aprobar o rechazar publicación", description = "decision APROBADA o RECHAZADA; el rechazo exige motivo (SR-RF-61).")
    @ApiResponse(responseCode = "200", description = "Publicación moderada")
    @ApiResponse(responseCode = "400", description = "Campos inválidos")
    @ApiResponse(responseCode = "404", description = "La prenda no existe")
    @ApiResponse(responseCode = "422", description = "La prenda no está pendiente o falta el motivo")
    ResponseEntity<PrendaResponse> moderar(UUID id, ModeracionRequest request);
}
