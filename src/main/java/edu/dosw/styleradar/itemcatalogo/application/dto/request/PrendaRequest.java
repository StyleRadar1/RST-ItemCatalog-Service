package edu.dosw.styleradar.itemcatalogo.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

// Al editar se ignora almacenId
public record PrendaRequest(
        @NotNull UUID almacenId,
        @NotBlank @Size(max = 120) String nombre,
        @NotBlank @Size(max = 60) String tipo,
        @Size(max = 40) String color,
        @Size(max = 60) String marca,
        @Size(max = 60) String estilo,
        @NotNull @Positive BigDecimal precio,
        @NotEmpty List<@NotBlank @Size(max = 500) String> fotografias) {
}
