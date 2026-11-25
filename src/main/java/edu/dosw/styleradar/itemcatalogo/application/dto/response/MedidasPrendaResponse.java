package edu.dosw.styleradar.itemcatalogo.application.dto.response;

import java.util.List;
import java.util.UUID;

public record MedidasPrendaResponse(UUID garmentId, List<MedidaTalla> sizes) {

    public record MedidaTalla(String size, Double minChestCm, Double maxChestCm, Double minWaistCm, Double maxWaistCm,
                              Double minHipCm, Double maxHipCm, Double lengthCm) {
    }
}
