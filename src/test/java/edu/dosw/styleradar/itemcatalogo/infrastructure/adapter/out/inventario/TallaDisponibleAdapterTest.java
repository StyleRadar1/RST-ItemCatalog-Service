package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.inventario;

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

class TallaDisponibleAdapterTest {

    private final InventarioClient client = mock(InventarioClient.class);
    private final TallaDisponibleAdapter adapter = new TallaDisponibleAdapter(client);
    private final UUID prendaId = UUID.randomUUID();

    private DisponibilidadPrendaResponse.Sede sede(boolean activo, String talla, int unidades) {
        return new DisponibilidadPrendaResponse.Sede(UUID.randomUUID(), activo,
                List.of(new DisponibilidadPrendaResponse.Variante(talla, unidades)));
    }

    @Test
    void hayStockSiAlgunaSedeActivaTieneUnidadesDeLaTalla() {
        when(client.disponibilidad(prendaId)).thenReturn(new DisponibilidadPrendaResponse(prendaId,
                List.of(sede(true, "S", 4), sede(true, "M", 2))));

        assertThat(adapter.hayStock(prendaId, "m")).isTrue();
    }

    @Test
    void noHayStockSiLaTallaEstaAgotadaOSoloEstaEnSedesInactivas() {
        when(client.disponibilidad(prendaId)).thenReturn(new DisponibilidadPrendaResponse(prendaId,
                List.of(sede(true, "M", 0), sede(false, "M", 5))));

        assertThat(adapter.hayStock(prendaId, "M")).isFalse();
    }

    @Test
    void noHayStockSiLaPrendaNoTieneInventario() {
        Request request = Request.create(Request.HttpMethod.GET, "/inventario/prenda/" + prendaId, Map.of(), null,
                StandardCharsets.UTF_8, null);
        when(client.disponibilidad(prendaId)).thenThrow(new FeignException.NotFound("Not Found", request, null, Map.of()));

        assertThat(adapter.hayStock(prendaId, "M")).isFalse();
    }
}
