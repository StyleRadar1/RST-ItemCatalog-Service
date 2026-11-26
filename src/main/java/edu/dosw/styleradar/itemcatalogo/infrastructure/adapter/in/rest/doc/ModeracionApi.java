package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.ModeracionRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PendientesResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Moderation", description = "Publication review by the administrator")
public interface ModeracionApi {

    @Operation(summary = "Review pending publications by group",
            description = "Pending store publications grouped by store, from most to least (SR-RF-87). "
                    + "User publications are second-hand and are moderated in ms-segunda-vida-donaciones.")
    @ApiResponse(responseCode = "200", description = "Pending publications grouped by store")
    @ApiResponse(responseCode = "403", description = "The user is not a StyleRadar administrator")
    ResponseEntity<PendientesResponse> pendientes(@Parameter(description = "Role sent by the API Gateway; STYLERADAR_ADMIN (or the former ADMIN_STYLERADAR or ADMIN)") String rol, @Parameter(description = "All the user roles sent by the API Gateway") String roles);

    @Operation(summary = "Approve or reject a group of publications",
            description = "Applies the same decision to every pending publication of the store; a rejection requires a reason (SR-RF-87).")
    @ApiResponse(responseCode = "200", description = "Group publications moderated")
    @ApiResponse(responseCode = "400", description = "Invalid fields")
    @ApiResponse(responseCode = "404", description = "The store has no pending publications")
    @ApiResponse(responseCode = "422", description = "The rejection reason is missing or the decision is not valid")
    @ApiResponse(responseCode = "403", description = "The user is not a StyleRadar administrator")
    ResponseEntity<List<PrendaResponse>> moderarGrupo(@Parameter(description = "Store id or NIT") String storeId, @Parameter(description = "Role sent by the API Gateway; STYLERADAR_ADMIN (or the former ADMIN_STYLERADAR or ADMIN)") String rol, @Parameter(description = "All the user roles sent by the API Gateway") String roles,
                                                      @Parameter(description = "User id sent by the API Gateway; it is the moderator when present") String usuarioId, ModeracionRequest request);

    @Operation(summary = "Approve or reject a publication", description = "decision APROBADA or RECHAZADA; a rejection requires a reason (SR-RF-61). The moderator is the authenticated user (X-User-Id); moderadorId (UUID) is only used when calling the service directly.")
    @ApiResponse(responseCode = "200", description = "Publication moderated")
    @ApiResponse(responseCode = "400", description = "Invalid fields")
    @ApiResponse(responseCode = "404", description = "The garment does not exist")
    @ApiResponse(responseCode = "422", description = "The garment is not pending or the reason is missing")
    @ApiResponse(responseCode = "403", description = "The user is not a StyleRadar administrator")
    ResponseEntity<PrendaResponse> moderar(UUID id, @Parameter(description = "Role sent by the API Gateway; STYLERADAR_ADMIN (or the former ADMIN_STYLERADAR or ADMIN)") String rol, @Parameter(description = "All the user roles sent by the API Gateway") String roles,
                                           @Parameter(description = "User id sent by the API Gateway; it is the moderator when present") String usuarioId, ModeracionRequest request);
}
