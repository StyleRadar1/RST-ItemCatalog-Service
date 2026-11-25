package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.inventario;

import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StockPrendaAdapterTest {

    private final InventarioClient client = mock(InventarioClient.class);
    private final StockPrendaAdapter adapter = new StockPrendaAdapter(client);
    private final UUID prendaId = UUID.randomUUID();

    @Test
    void sumaLasTallasConStockDeTodasLasSedes() {
        when(client.disponibilidad(prendaId)).thenReturn(new DisponibilidadInventarioResponse(prendaId, 7, List.of(
                new DisponibilidadInventarioResponse.Sede(UUID.randomUUID(), List.of(
                        new DisponibilidadInventarioResponse.Variante("M", 4),
                        new DisponibilidadInventarioResponse.Variante("L", 0))),
                new DisponibilidadInventarioResponse.Sede(UUID.randomUUID(), List.of(
                        new DisponibilidadInventarioResponse.Variante("S", 3),
                        new DisponibilidadInventarioResponse.Variante("M", 0))),
                new DisponibilidadInventarioResponse.Sede(UUID.randomUUID(), null))));

        assertThat(adapter.disponibilidad(prendaId)).contains(new Disponibilidad(7, List.of("M", "S")));
    }

    @Test
    void sinSedesNoHayTallas() {
        when(client.disponibilidad(prendaId)).thenReturn(new DisponibilidadInventarioResponse(prendaId, 0, null));

        assertThat(adapter.disponibilidad(prendaId)).contains(Disponibilidad.sinStock());
    }

    @Test
    void siInventarioNoTieneLaPrendaNoHayDisponibilidad() {
        Request request = Request.create(Request.HttpMethod.GET, "/inventory/garments/" + prendaId, Map.of(), null,
                StandardCharsets.UTF_8, null);
        when(client.disponibilidad(prendaId)).thenThrow(new FeignException.NotFound("no", request, null, Map.of()));

        assertThat(adapter.disponibilidad(prendaId)).isEmpty();
    }

    @Test
    void respuestaVaciaNoHayDisponibilidad() {
        when(client.disponibilidad(prendaId)).thenReturn(null);

        assertThat(adapter.disponibilidad(prendaId)).isEmpty();
    }
}
