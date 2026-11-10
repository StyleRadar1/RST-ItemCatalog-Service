package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Tag(name = "Estado", description = "Verificación del estado de ms-item-catalogo")
public interface HealthApi {

    @Operation(summary = "Consultar el estado del servicio",
            description = "Indica si ms-item-catalogo está en ejecución y responde en el puerto 8220.")
    @ApiResponse(responseCode = "200", description = "Servicio en ejecución",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"status\":\"UP\",\"service\":\"ms-item-catalogo\",\"port\":8220}")))
    @ApiResponse(responseCode = "500", description = "Error interno del servicio",
            content = @Content(mediaType = "application/problem+json"))
    ResponseEntity<Map<String, Object>> health();
}
