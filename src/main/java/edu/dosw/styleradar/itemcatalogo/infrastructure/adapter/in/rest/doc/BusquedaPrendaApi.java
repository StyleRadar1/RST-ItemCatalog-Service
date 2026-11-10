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

@Tag(name = "Búsqueda", description = "Búsqueda y filtros del catálogo")
public interface BusquedaPrendaApi {

    @Operation(summary = "Buscar prendas",
            description = "Busca por texto libre y filtra por tipo, color, marca, estilo, talla disponible y rango de precio. Ordena por recientes, por reputación del almacén o por distancia a la ubicación indicada (SR-RF-07 a 15).")
    @ApiResponse(responseCode = "200", description = "Prendas aprobadas que cumplen el filtro")
    @ApiResponse(responseCode = "400", description = "Algún parámetro tiene un formato inválido o se pidió orden por distancia sin ubicación")
    ResponseEntity<List<PrendaResponse>> buscar(
            @Parameter(description = "Texto libre", example = "chaqueta") String texto,
            @Parameter(example = "Chaqueta") String tipo,
            @Parameter(example = "Azul") String color,
            @Parameter(example = "Levis") String marca,
            @Parameter(example = "Urbano") String estilo,
            @Parameter(description = "Solo prendas con unidades disponibles en esta talla, según ms-inventario", example = "M") String talla,
            @Parameter(example = "50000") BigDecimal precioMin,
            @Parameter(example = "200000") BigDecimal precioMax,
            @Parameter(description = "RECIENTES (por defecto), REPUTACION o DISTANCIA") Ordenamiento orden,
            @Parameter(description = "Latitud del usuario, obligatoria para ordenar por distancia", example = "4.6683") Double lat,
            @Parameter(description = "Longitud del usuario, obligatoria para ordenar por distancia", example = "-74.0546") Double lng);

    @Operation(summary = "Consultar prendas similares",
            description = "Prendas del mismo tipo o estilo, para cuando no hay coincidencia exacta (SR-RF-16).")
    @ApiResponse(responseCode = "200", description = "Prendas similares")
    @ApiResponse(responseCode = "404", description = "La prenda no existe")
    ResponseEntity<List<PrendaResponse>> similares(UUID id);
}
