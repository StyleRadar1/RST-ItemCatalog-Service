package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.PrendaRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Garments", description = "Publishing and managing store garments")
public interface PrendaApi {

    @Operation(summary = "Publish garment",
            description = "Registers the garment with its photos, price and attributes. When the API Gateway sends X-User-Id, the store is the one the user manages in ms-tiendas; otherwise almacenId from the body is used. It stays PENDIENTE until moderated; once approved, styleradar.catalogo.prenda.publicada is published (SR-RF-50, 51, 53 and 54).")
    @ApiResponse(responseCode = "201", description = "Garment published")
    @ApiResponse(responseCode = "400", description = "Invalid fields")
    @ApiResponse(responseCode = "404", description = "The store does not exist")
    @ApiResponse(responseCode = "422", description = "The store is suspended")
    ResponseEntity<PrendaResponse> publicar(PrendaRequest request,
                                            @Parameter(description = "User id sent by the API Gateway; used to find the user store in ms-tiendas") String usuarioId);

    @Operation(summary = "Get garment")
    @ApiResponse(responseCode = "200", description = "Garment detail")
    @ApiResponse(responseCode = "404", description = "The garment does not exist")
    ResponseEntity<PrendaResponse> obtener(UUID id);

    @Operation(summary = "Edit garment",
            description = "Only the owner store (X-Almacen-Id or the user store in ms-tiendas) or an administrator. Updates the garment and sends it back to moderation (SR-RF-55).")
    @ApiResponse(responseCode = "200", description = "Garment edited")
    @ApiResponse(responseCode = "400", description = "Invalid fields")
    @ApiResponse(responseCode = "403", description = "Not the owner store nor an administrator")
    @ApiResponse(responseCode = "404", description = "The garment does not exist")
    @ApiResponse(responseCode = "422", description = "The garment is withdrawn")
    ResponseEntity<PrendaResponse> editar(UUID id,
                                          @Parameter(description = "Store making the request (sent by the API Gateway)") UUID almacenId,
                                          @Parameter(description = "Role sent by the API Gateway; ADMIN or ADMIN_STYLERADAR") String rol,
                                          @Parameter(description = "All the user roles sent by the API Gateway") String roles,
                                          @Parameter(description = "User id sent by the API Gateway; used to find the user store in ms-tiendas") String usuarioId,
                                          PrendaRequest request);

    @Operation(summary = "Withdraw garment",
            description = "Only the owner store (X-Almacen-Id or the user store in ms-tiendas) or an administrator. The garment is no longer shown and styleradar.catalogo.prenda.retirada is published (SR-RF-58).")
    @ApiResponse(responseCode = "200", description = "Garment withdrawn")
    @ApiResponse(responseCode = "403", description = "Not the owner store nor an administrator")
    @ApiResponse(responseCode = "404", description = "The garment does not exist")
    @ApiResponse(responseCode = "422", description = "The garment was already withdrawn")
    ResponseEntity<PrendaResponse> retirar(UUID id,
                                           @Parameter(description = "Store making the request (sent by the API Gateway)") UUID almacenId,
                                           @Parameter(description = "Role sent by the API Gateway; ADMIN or ADMIN_STYLERADAR") String rol,
                                           @Parameter(description = "All the user roles sent by the API Gateway") String roles,
                                           @Parameter(description = "User id sent by the API Gateway; used to find the user store in ms-tiendas") String usuarioId);
}
