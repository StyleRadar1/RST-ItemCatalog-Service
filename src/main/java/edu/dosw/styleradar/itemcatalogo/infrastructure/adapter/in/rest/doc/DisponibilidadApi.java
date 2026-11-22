package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.itemcatalogo.application.dto.response.DisponibilidadResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.TallasMedidasResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Availability", description = "Garment stock for other services")
public interface DisponibilidadApi {

    @Operation(summary = "Get garment availability",
            description = "Total units and sizes with stock, taken from ms-inventario. Used by ms-notificaciones; "
                    + "if ms-inventario has no stock for the garment, stockTotal is 0.")
    @ApiResponse(responseCode = "200", description = "Garment availability")
    @ApiResponse(responseCode = "400", description = "Invalid garment id")
    @ApiResponse(responseCode = "404", description = "The garment does not exist")
    ResponseEntity<DisponibilidadResponse> disponibilidad(UUID itemId);

    @Operation(summary = "Get offered sizes and measurement chart",
            description = "Used by ms-probador-virtual (SR-RF-24). availableSizes lists the sizes with stock in ms-inventario "
                    + "([] when none, null when ms-inventario has no record for the garment). sizes is always [] because the "
                    + "catalog does not store measurement charts in cm.")
    @ApiResponse(responseCode = "200", description = "Offered sizes and measurement chart")
    @ApiResponse(responseCode = "400", description = "Invalid garment id")
    @ApiResponse(responseCode = "404", description = "The garment does not exist")
    ResponseEntity<TallasMedidasResponse> tallasMedidas(UUID itemId);
}
