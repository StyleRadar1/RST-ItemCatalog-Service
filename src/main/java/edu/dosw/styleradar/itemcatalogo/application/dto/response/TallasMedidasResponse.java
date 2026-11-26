package edu.dosw.styleradar.itemcatalogo.application.dto.response;

import java.util.List;

// Contrato de ms-probador-virtual (SR-RF-24); los nombres van en inglés porque así los lee el probador
public record TallasMedidasResponse(List<String> availableSizes, List<RangoMedidas> sizes) {

    public record RangoMedidas(String size, Double minChestCm, Double maxChestCm, Double minWaistCm,
                               Double maxWaistCm, Double minHipCm, Double maxHipCm, Double lengthCm) {

        public RangoMedidas(String size, Double minChestCm, Double maxChestCm, Double minWaistCm,
                            Double maxWaistCm, Double minHipCm, Double maxHipCm) {
            this(size, minChestCm, maxChestCm, minWaistCm, maxWaistCm, minHipCm, maxHipCm, null);
        }
    }
}
