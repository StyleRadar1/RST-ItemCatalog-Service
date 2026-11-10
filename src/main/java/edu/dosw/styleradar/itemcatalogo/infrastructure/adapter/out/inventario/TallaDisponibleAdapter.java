package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.inventario;

import edu.dosw.styleradar.itemcatalogo.domain.port.out.TallaDisponiblePort;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class TallaDisponibleAdapter implements TallaDisponiblePort {

    private final InventarioClient inventarioClient;

    public TallaDisponibleAdapter(InventarioClient inventarioClient) {
        this.inventarioClient = inventarioClient;
    }

    @Override
    public Set<UUID> prendasConStock(String talla) {
        return new HashSet<>(inventarioClient.prendasDisponibles(talla));
    }
}
