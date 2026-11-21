package edu.dosw.styleradar.itemcatalogo.domain.model;

import java.util.List;

public record Disponibilidad(int stockTotal, List<String> tallasConStock) {

    public static Disponibilidad sinStock() {
        return new Disponibilidad(0, List.of());
    }
}
