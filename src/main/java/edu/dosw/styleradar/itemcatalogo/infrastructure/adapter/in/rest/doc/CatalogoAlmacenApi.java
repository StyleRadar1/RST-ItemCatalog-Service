package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.itemcatalogo.application.dto.response.CatalogoResumenResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Store catalog", description = "Catalog, summary and new arrivals of a store")
public interface CatalogoAlmacenApi {

    @Operation(summary = "Get a store catalog", description = "Approved and available garments of the store (SR-RF-22).")
    @ApiResponse(responseCode = "200", description = "Store catalog")
    @ApiResponse(responseCode = "404", description = "The store does not exist")
    ResponseEntity<List<PrendaResponse>> catalogo(@Parameter(description = "Store id or NIT") String storeId);

    @Operation(summary = "Get catalog summary", description = "Total garments and the 4 most recent ones, for the map (SR-RF-19).")
    @ApiResponse(responseCode = "200", description = "Catalog summary")
    @ApiResponse(responseCode = "404", description = "The store does not exist")
    ResponseEntity<CatalogoResumenResponse> resumen(@Parameter(description = "Store id or NIT") String storeId);

    @Operation(summary = "Get weekly new arrivals", description = "Up to 3 garments published in the last 7 days, newest first (SR-RF-20).")
    @ApiResponse(responseCode = "200", description = "Store new arrivals")
    @ApiResponse(responseCode = "404", description = "The store does not exist")
    ResponseEntity<List<PrendaResponse>> novedades(@Parameter(description = "Store id or NIT") String storeId);
}
