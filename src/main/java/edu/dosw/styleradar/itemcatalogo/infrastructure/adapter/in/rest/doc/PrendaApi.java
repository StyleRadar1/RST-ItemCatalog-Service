package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.PrendaRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Prendas", description = "Publicación y gestión de prendas del almacén")
public interface PrendaApi {

    @Operation(summary = "Publicar prenda",
            description = "Registra la prenda con sus fotografías, precio y atributos. Queda PENDIENTE de moderación (SR-RF-50, 51, 53 y 54).")
    @ApiResponse(responseCode = "201", description = "Prenda publicada")
    @ApiResponse(responseCode = "400", description = "Campos inválidos")
    @ApiResponse(responseCode = "404", description = "El almacén no existe")
    @ApiResponse(responseCode = "422", description = "El almacén está suspendido")
    ResponseEntity<PrendaResponse> publicar(PrendaRequest request);

    @Operation(summary = "Consultar prenda")
    @ApiResponse(responseCode = "200", description = "Detalle de la prenda")
    @ApiResponse(responseCode = "404", description = "La prenda no existe")
    ResponseEntity<PrendaResponse> obtener(UUID id);

    @Operation(summary = "Editar prenda", description = "Actualiza la prenda y la devuelve a moderación (SR-RF-55).")
    @ApiResponse(responseCode = "200", description = "Prenda editada")
    @ApiResponse(responseCode = "400", description = "Campos inválidos")
    @ApiResponse(responseCode = "404", description = "La prenda no existe")
    @ApiResponse(responseCode = "422", description = "La prenda está retirada")
    ResponseEntity<PrendaResponse> editar(UUID id, PrendaRequest request);

    @Operation(summary = "Retirar prenda", description = "La prenda deja de mostrarse en el catálogo (SR-RF-58).")
    @ApiResponse(responseCode = "200", description = "Prenda retirada")
    @ApiResponse(responseCode = "404", description = "La prenda no existe")
    @ApiResponse(responseCode = "422", description = "La prenda ya estaba retirada")
    ResponseEntity<PrendaResponse> retirar(UUID id);
}
