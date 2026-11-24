package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.inventario;

import java.util.List;
import java.util.UUID;

public record DisponibilidadInventarioResponse(UUID prendaId, int stockDisponibleTotal, List<Sede> sedes) {

    public record Sede(UUID sedeId, List<Variante> variantes) {
    }

    public record Variante(String talla, int stockDisponible) {
    }
}
