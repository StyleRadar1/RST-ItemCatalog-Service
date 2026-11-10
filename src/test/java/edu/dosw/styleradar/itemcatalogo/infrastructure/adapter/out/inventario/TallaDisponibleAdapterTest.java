package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.inventario;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TallaDisponibleAdapterTest {

    @Test
    void consultaEnMsInventarioLasPrendasConStockEnLaTalla() {
        InventarioClient client = mock(InventarioClient.class);
        UUID prendaId = UUID.randomUUID();
        when(client.prendasDisponibles("M")).thenReturn(List.of(prendaId));

        assertThat(new TallaDisponibleAdapter(client).prendasConStock("M")).containsExactly(prendaId);
    }
}
