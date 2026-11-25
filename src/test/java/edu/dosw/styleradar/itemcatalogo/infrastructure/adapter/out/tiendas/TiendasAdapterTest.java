package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.tiendas;

import edu.dosw.styleradar.itemcatalogo.domain.port.out.TiendasPort;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TiendasAdapterTest {

    private final TiendasClient client = mock(TiendasClient.class);
    private final TiendasAdapter adapter = new TiendasAdapter(client);

    @Test
    void recorreTodasLasPaginasDeAlmacenes() {
        when(client.almacenes(0, TiendasAdapter.TAMANO_PAGINA)).thenReturn(new PaginaTiendasResponse(
                List.of(new AlmacenTiendasResponse("900123456-1", "Atelier", 4.876)), false));
        when(client.almacenes(1, TiendasAdapter.TAMANO_PAGINA)).thenReturn(new PaginaTiendasResponse(
                List.of(new AlmacenTiendasResponse("800555111-2", "Calle 85", null)), true));

        List<TiendasPort.AlmacenTienda> almacenes = adapter.almacenes();

        assertThat(almacenes).hasSize(2);
        assertThat(almacenes.get(0).id()).isEqualTo(TiendasAdapter.idAlmacen("900123456-1"));
        assertThat(almacenes.get(0).reputacion()).isEqualByComparingTo("4.88");
        assertThat(almacenes.get(0).nit()).isEqualTo("900123456-1");
        assertThat(almacenes.get(1).reputacion()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void terminaSiTiendasNoRespondeContenido() {
        when(client.almacenes(0, TiendasAdapter.TAMANO_PAGINA)).thenReturn(null);

        assertThat(adapter.almacenes()).isEmpty();
    }

    @Test
    void convierteElNitDelUsuarioEnElIdDelAlmacen() {
        when(client.almacenDelUsuario(10L)).thenReturn(new AlmacenTiendasResponse("900123456-1", "Atelier", 4.0));

        assertThat(adapter.almacenDelUsuario(10L)).contains(TiendasAdapter.idAlmacen("900123456-1"));
    }

    @Test
    void siTiendasFallaNoHayAlmacen() {
        Request request = Request.create(Request.HttpMethod.GET, "/users/10/store", Map.of(), null,
                StandardCharsets.UTF_8, null);
        when(client.almacenDelUsuario(10L)).thenThrow(new FeignException.NotFound("no", request, null, Map.of()));

        assertThat(adapter.almacenDelUsuario(10L)).isEmpty();
    }

    @Test
    void elIdEsElMismoQueCalculaGeolocalizacion() {
        UUID esperado = UUID.nameUUIDFromBytes("ms-tiendas:almacen:900123456-1".getBytes(StandardCharsets.UTF_8));

        assertThat(TiendasAdapter.idAlmacen(" 900123456-1 ")).isEqualTo(esperado);
        assertThat(TiendasAdapter.idAlmacen(null)).isNull();
        assertThat(TiendasAdapter.reputacion(9.5)).isEqualByComparingTo("5.00");
        assertThat(TiendasAdapter.reputacion(-1.0)).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
