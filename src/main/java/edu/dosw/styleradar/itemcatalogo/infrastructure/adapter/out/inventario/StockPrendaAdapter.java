package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.inventario;

import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.StockPrendaPort;
import feign.FeignException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class StockPrendaAdapter implements StockPrendaPort {

    private final InventarioClient inventarioClient;

    public StockPrendaAdapter(InventarioClient inventarioClient) {
        this.inventarioClient = inventarioClient;
    }

    @Override
    public Optional<Disponibilidad> disponibilidad(UUID prendaId) {
        DisponibilidadInventarioResponse respuesta;
        try {
            respuesta = inventarioClient.disponibilidad(prendaId);
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
        if (respuesta == null) {
            return Optional.empty();
        }
        List<String> tallas = respuesta.sedes() == null ? List.of() : respuesta.sedes().stream()
                .filter(Objects::nonNull)
                .flatMap(s -> s.variantes() == null ? Stream.empty() : s.variantes().stream())
                .filter(v -> v.stockDisponible() > 0 && v.talla() != null)
                .map(DisponibilidadInventarioResponse.Variante::talla)
                .distinct()
                .sorted()
                .toList();
        return Optional.of(new Disponibilidad(respuesta.stockDisponibleTotal(), tallas));
    }
}
