package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.inventario;

import java.util.List;
import java.util.UUID;

public record DisponibilidadPrendaResponse(UUID prendaId, List<Sede> sedes) {

    public record Sede(UUID sedeId, boolean activo, List<Variante> variantes) {
    }

    public record Variante(String talla, int stockDisponible) {
    }
}
