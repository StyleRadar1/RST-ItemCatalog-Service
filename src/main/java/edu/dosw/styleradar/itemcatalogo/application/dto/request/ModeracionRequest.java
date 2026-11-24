package edu.dosw.styleradar.itemcatalogo.application.dto.request;

import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// moderadorId es opcional: por el gateway el moderador sale del usuario autenticado (X-User-Id)
public record ModeracionRequest(
        @NotNull(message = "La decisión es obligatoria (APROBADA o RECHAZADA)")
        EstadoPublicacion decision,
        @Size(max = 300, message = "El motivo no puede superar los 300 caracteres")
        String motivo,
        String moderadorId) {
}
