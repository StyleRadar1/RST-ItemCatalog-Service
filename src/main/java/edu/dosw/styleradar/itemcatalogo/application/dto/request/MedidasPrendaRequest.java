package edu.dosw.styleradar.itemcatalogo.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record MedidasPrendaRequest(
        @NotEmpty(message = "Debe registrar las medidas de al menos una talla")
        @Size(max = 20, message = "Se pueden registrar medidas de máximo 20 tallas")
        List<@Valid @NotNull(message = "La medida de la talla es obligatoria") MedidaTalla> sizes) {

    public record MedidaTalla(
            @NotBlank(message = "La talla es obligatoria")
            String size,
            @Positive(message = "Las medidas deben ser mayores a cero") @DecimalMax(value = "300", message = "Las medidas no pueden superar 300 cm")
            Double minChestCm,
            @Positive(message = "Las medidas deben ser mayores a cero") @DecimalMax(value = "300", message = "Las medidas no pueden superar 300 cm")
            Double maxChestCm,
            @Positive(message = "Las medidas deben ser mayores a cero") @DecimalMax(value = "300", message = "Las medidas no pueden superar 300 cm")
            Double minWaistCm,
            @Positive(message = "Las medidas deben ser mayores a cero") @DecimalMax(value = "300", message = "Las medidas no pueden superar 300 cm")
            Double maxWaistCm,
            @Positive(message = "Las medidas deben ser mayores a cero") @DecimalMax(value = "300", message = "Las medidas no pueden superar 300 cm")
            Double minHipCm,
            @Positive(message = "Las medidas deben ser mayores a cero") @DecimalMax(value = "300", message = "Las medidas no pueden superar 300 cm")
            Double maxHipCm,
            @Positive(message = "Las medidas deben ser mayores a cero") @DecimalMax(value = "300", message = "Las medidas no pueden superar 300 cm")
            Double lengthCm) {
    }
}
