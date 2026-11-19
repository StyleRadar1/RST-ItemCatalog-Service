package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Tag(name = "Health", description = "ms-item-catalogo health check")
public interface HealthApi {

    @Operation(summary = "Get service status",
            description = "Shows whether ms-item-catalogo is running and answering on port 8220.")
    @ApiResponse(responseCode = "200", description = "Service running",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"status\":\"UP\",\"service\":\"ms-item-catalogo\",\"port\":8220}")))
    @ApiResponse(responseCode = "500", description = "Internal service error",
            content = @Content(mediaType = "application/problem+json"))
    ResponseEntity<Map<String, Object>> health();
}
