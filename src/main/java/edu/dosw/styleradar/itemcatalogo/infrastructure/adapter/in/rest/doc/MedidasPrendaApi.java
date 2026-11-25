package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.MedidasPrendaRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.MedidasPrendaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Measurements", description = "Garment measurements per size for the virtual try-on mannequin")
public interface MedidasPrendaApi {

    @Operation(summary = "Save garment measurements",
            description = "Replaces the measurements in cm of each size of the garment (chest, waist and hip ranges and "
                    + "length). Only the store that owns the garment or a StyleRadar admin. The size guide used by "
                    + "ms-probador-virtual returns these values instead of the reference guide.")
    @ApiResponse(responseCode = "200", description = "Measurements saved")
    @ApiResponse(responseCode = "400", description = "Invalid data")
    @ApiResponse(responseCode = "403", description = "Not the owner store or an admin")
    @ApiResponse(responseCode = "404", description = "The garment does not exist")
    @ApiResponse(responseCode = "422", description = "Invalid size, repeated size, min greater than max or withdrawn garment")
    ResponseEntity<MedidasPrendaResponse> registrar(UUID id, UUID almacenId, String rol, String roles, String usuarioId,
                                                    MedidasPrendaRequest request);

    @Operation(summary = "Get garment measurements",
            description = "Measurements in cm saved for each size of the garment; empty list when none were saved.")
    @ApiResponse(responseCode = "200", description = "Garment measurements")
    @ApiResponse(responseCode = "400", description = "Invalid garment id")
    @ApiResponse(responseCode = "404", description = "The garment does not exist")
    ResponseEntity<MedidasPrendaResponse> consultar(UUID id);
}
