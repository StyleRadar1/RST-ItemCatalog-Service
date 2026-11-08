package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.geolocalizacion;

import edu.dosw.styleradar.itemcatalogo.domain.model.Ubicacion;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DistanciaAlmacenAdapterTest {

    @Test
    void consultaLaDistanciaEnMsGeolocalizacion() {
        GeolocalizacionClient client = mock(GeolocalizacionClient.class);
        UUID almacenId = UUID.randomUUID();
        when(client.distancia(almacenId, 4.6683, -74.0546)).thenReturn(new DistanciaAlmacenResponse(almacenId, 1.2));

        double kilometros = new DistanciaAlmacenAdapter(client).kilometros(almacenId, new Ubicacion(4.6683, -74.0546));

        assertThat(kilometros).isEqualTo(1.2);
    }
}
