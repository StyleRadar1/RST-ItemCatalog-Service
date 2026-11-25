package edu.dosw.styleradar.itemcatalogo.application.mapper.in;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.MedidasPrendaRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.MedidasPrendaResponse;
import edu.dosw.styleradar.itemcatalogo.domain.model.MedidaPrenda;

import java.util.List;
import java.util.UUID;

public final class MedidaPrendaMapperIn {

    private MedidaPrendaMapperIn() {
    }

    public static List<MedidaPrenda> toDomain(MedidasPrendaRequest request) {
        return request.sizes().stream()
                .map(m -> new MedidaPrenda(m.size(), m.minChestCm(), m.maxChestCm(), m.minWaistCm(), m.maxWaistCm(),
                        m.minHipCm(), m.maxHipCm(), m.lengthCm()))
                .toList();
    }

    public static MedidasPrendaResponse toResponse(UUID prendaId, List<MedidaPrenda> medidas) {
        return new MedidasPrendaResponse(prendaId, medidas.stream()
                .map(m -> new MedidasPrendaResponse.MedidaTalla(m.talla(), m.minPecho(), m.maxPecho(), m.minCintura(),
                        m.maxCintura(), m.minCadera(), m.maxCadera(), m.largo()))
                .toList());
    }
}
