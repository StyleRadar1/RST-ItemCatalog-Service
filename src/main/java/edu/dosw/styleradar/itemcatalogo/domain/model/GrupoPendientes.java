package edu.dosw.styleradar.itemcatalogo.domain.model;

import java.util.List;
import java.util.UUID;

public record GrupoPendientes(UUID almacenId, String almacen, List<Prenda> prendas) {

    public int total() {
        return prendas.size();
    }
}
