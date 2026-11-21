package edu.dosw.styleradar.itemcatalogo.application.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PrendaRequest(
        UUID almacenId,
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 60, message = "El nombre debe tener entre 3 y 60 caracteres")
        String nombre,
        @NotBlank(message = "El tipo de prenda es obligatorio")
        @Size(max = 20, message = "El tipo no puede superar los 20 caracteres")
        String tipo,
        @NotBlank(message = "El color es obligatorio")
        @Size(max = 20, message = "El color no puede superar los 20 caracteres")
        String color,
        @NotBlank(message = "La marca es obligatoria")
        String marca,
        @NotBlank(message = "El estilo es obligatorio")
        @Size(max = 20, message = "El estilo no puede superar los 20 caracteres")
        String estilo,
        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor a cero")
        @DecimalMax(value = "10000000", message = "El precio no puede superar 10.000.000 COP")
        @Digits(integer = 8, fraction = 0, message = "El precio no puede tener decimales")
        BigDecimal precio,
        @NotEmpty(message = "La prenda debe tener al menos una fotografía")
        @Size(max = 5, message = "La prenda puede tener máximo 5 fotografías")
        List<@NotBlank(message = "La URL de la fotografía es obligatoria")
             @Size(max = 300, message = "La URL de la fotografía no puede superar los 300 caracteres") String> fotografias) {
}
