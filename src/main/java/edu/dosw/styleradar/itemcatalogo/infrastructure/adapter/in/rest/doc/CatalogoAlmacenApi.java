package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.itemcatalogo.application.dto.response.CatalogoResumenResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Catálogo de almacén", description = "Catálogo, resumen y novedades de un almacén")
public interface CatalogoAlmacenApi {

    @Operation(summary = "Consultar catálogo de un almacén", description = "Prendas aprobadas y disponibles del almacén (SR-RF-22).")
    @ApiResponse(responseCode = "200", description = "Catálogo del almacén")
    @ApiResponse(responseCode = "404", description = "El almacén no existe")
    ResponseEntity<List<PrendaResponse>> catalogo(UUID almacenId);

    @Operation(summary = "Consultar catálogo resumido", description = "Total de prendas y las 4 más recientes, para el mapa (SR-RF-19).")
    @ApiResponse(responseCode = "200", description = "Resumen del catálogo")
    @ApiResponse(responseCode = "404", description = "El almacén no existe")
    ResponseEntity<CatalogoResumenResponse> resumen(UUID almacenId);

    @Operation(summary = "Consultar novedades semanales", description = "Prendas publicadas en los últimos 7 días (SR-RF-20).")
    @ApiResponse(responseCode = "200", description = "Novedades del almacén")
    @ApiResponse(responseCode = "404", description = "El almacén no existe")
    ResponseEntity<List<PrendaResponse>> novedades(UUID almacenId);
}
