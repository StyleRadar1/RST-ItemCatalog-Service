package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.ModeracionRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PendientesResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Moderación", description = "Revisión de publicaciones por el administrador")
public interface ModeracionApi {

    @Operation(summary = "Revisar publicaciones pendientes por grupos",
            description = "Número de publicaciones pendientes de las tiendas, agrupadas por almacén, de mayor a menor (SR-RF-87). "
                    + "Las publicaciones de usuarios son de segunda mano y se moderan en ms-segunda-vida-donaciones.")
    @ApiResponse(responseCode = "200", description = "Pendientes agrupadas por almacén")
    ResponseEntity<PendientesResponse> pendientes();

    @Operation(summary = "Aprobar o rechazar las publicaciones de un grupo",
            description = "Aplica la misma decisión a todas las publicaciones pendientes del almacén; el rechazo exige motivo (SR-RF-87).")
    @ApiResponse(responseCode = "200", description = "Publicaciones del grupo moderadas")
    @ApiResponse(responseCode = "400", description = "Campos inválidos")
    @ApiResponse(responseCode = "404", description = "El almacén no tiene publicaciones pendientes")
    @ApiResponse(responseCode = "422", description = "Falta el motivo del rechazo o la decisión no es válida")
    ResponseEntity<List<PrendaResponse>> moderarGrupo(UUID almacenId, ModeracionRequest request);

    @Operation(summary = "Aprobar o rechazar publicación", description = "decision APROBADA o RECHAZADA; el rechazo exige motivo (SR-RF-61).")
    @ApiResponse(responseCode = "200", description = "Publicación moderada")
    @ApiResponse(responseCode = "400", description = "Campos inválidos")
    @ApiResponse(responseCode = "404", description = "La prenda no existe")
    @ApiResponse(responseCode = "422", description = "La prenda no está pendiente o falta el motivo")
    ResponseEntity<PrendaResponse> moderar(UUID id, ModeracionRequest request);
}
