package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.inventario;

import edu.dosw.styleradar.itemcatalogo.domain.port.out.TallaDisponiblePort;
import feign.FeignException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TallaDisponibleAdapter implements TallaDisponiblePort {

    private final InventarioClient inventarioClient;

    public TallaDisponibleAdapter(InventarioClient inventarioClient) {
        this.inventarioClient = inventarioClient;
    }

    @Override
    public boolean hayStock(UUID prendaId, String talla) {
        try {
            return inventarioClient.disponibilidad(prendaId).sedes().stream()
                    .filter(DisponibilidadPrendaResponse.Sede::activo)
                    .flatMap(sede -> sede.variantes().stream())
                    .anyMatch(variante -> variante.talla().equalsIgnoreCase(talla) && variante.stockDisponible() > 0);
        } catch (FeignException.NotFound e) {
            return false;
        }
    }
}
