package edu.dosw.styleradar.itemcatalogo.application.dto.request;

import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ModeracionRequest(
        @NotNull(message = "La decisión es obligatoria (APROBADA o RECHAZADA)")
        EstadoPublicacion decision,
        @Size(max = 300, message = "El motivo no puede superar los 300 caracteres")
        String motivo,
        @NotNull(message = "El moderador es obligatorio")
        UUID moderadorId) {
}
