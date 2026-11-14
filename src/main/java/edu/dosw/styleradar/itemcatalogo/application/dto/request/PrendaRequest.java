package edu.dosw.styleradar.itemcatalogo.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PrendaRequest(
        @NotNull(message = "El almacén es obligatorio")
        UUID almacenId,
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
        String nombre,
        @NotBlank(message = "El tipo de prenda es obligatorio")
        @Size(max = 60, message = "El tipo no puede superar los 60 caracteres")
        String tipo,
        @Size(max = 40, message = "El color no puede superar los 40 caracteres")
        String color,
        @Size(max = 60, message = "La marca no puede superar los 60 caracteres")
        String marca,
        @Size(max = 60, message = "El estilo no puede superar los 60 caracteres")
        String estilo,
        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor a cero")
        BigDecimal precio,
        @NotEmpty(message = "La prenda debe tener al menos una fotografía")
        List<@NotBlank(message = "La URL de la fotografía es obligatoria")
             @Size(max = 500, message = "La URL de la fotografía no puede superar los 500 caracteres") String> fotografias) {
}
