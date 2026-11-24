package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import edu.dosw.styleradar.itemcatalogo.domain.model.Ordenamiento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Tag(name = "Search", description = "Catalog search and filters")
public interface BusquedaPrendaApi {

    @Operation(summary = "Search garments",
            description = "Searches by free text and filters by type, color, brand, style, available size and price range. Sorts by most recent, store rating or distance to the given location; with lat and lng each garment includes distanciaKm (SR-RF-07 to 15).")
    @ApiResponse(responseCode = "200", description = "Approved garments that match the filter")
    @ApiResponse(responseCode = "400", description = "A parameter is invalid or not in its list, or distance sorting was requested without a location")
    ResponseEntity<List<PrendaResponse>> buscar(
            @Parameter(description = "Free text", example = "chaqueta") String texto,
            @Parameter(description = "Garment type from the unified list", example = "Chaqueta") String tipo,
            @Parameter(description = "Color from the unified list", example = "Azul") String color,
            @Parameter(description = "Brand: one of the allied stores", example = "Luna Store") String marca,
            @Parameter(description = "Style: CASUAL, FORMAL, DEPORTIVO, URBANO or VINTAGE", example = "URBANO") String estilo,
            @Parameter(description = "Only garments with available units in this size, according to ms-inventario", example = "M") String talla,
            @Parameter(description = "Minimum price in COP", example = "50000") BigDecimal precioMin,
            @Parameter(description = "Maximum price in COP", example = "200000") BigDecimal precioMax,
            @Parameter(description = "RECIENTES (default), REPUTACION or DISTANCIA") Ordenamiento orden,
            @Parameter(description = "User latitude, required to sort by distance", example = "4.6683") Double lat,
            @Parameter(description = "User longitude, required to sort by distance", example = "-74.0546") Double lng);

    @Operation(summary = "Get similar garments",
            description = "Up to 10 garments of the same type or style, for when there is no exact match (SR-RF-16).")
    @ApiResponse(responseCode = "200", description = "Similar garments")
    @ApiResponse(responseCode = "404", description = "The garment does not exist")
    ResponseEntity<List<PrendaResponse>> similares(UUID id);
}
