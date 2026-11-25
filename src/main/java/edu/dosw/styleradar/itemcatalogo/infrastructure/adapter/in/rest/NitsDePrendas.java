package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarNitAlmacenUseCase;

import java.util.List;
import java.util.Map;
import java.util.UUID;

final class NitsDePrendas {

    private NitsDePrendas() {
    }

    static Map<UUID, String> de(ConsultarNitAlmacenUseCase nitAlmacenes, List<Prenda> prendas) {
        return nitAlmacenes.nitsDe(prendas.stream().map(Prenda::getAlmacenId).toList());
    }
}
