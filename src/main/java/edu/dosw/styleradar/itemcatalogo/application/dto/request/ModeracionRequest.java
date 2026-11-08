package edu.dosw.styleradar.itemcatalogo.application.dto.request;

import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

// decision: APROBADA o RECHAZADA; el motivo es obligatorio al rechazar
public record ModeracionRequest(
        @NotNull EstadoPublicacion decision,
        @Size(max = 300) String motivo,
        @NotNull UUID moderadorId) {
}
