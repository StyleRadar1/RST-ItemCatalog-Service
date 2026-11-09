package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.amqp;

import edu.dosw.styleradar.itemcatalogo.domain.port.in.ActualizarDisponibilidadUseCase;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class CatalogoEventListenerTest {

    private final ActualizarDisponibilidadUseCase useCase = mock(ActualizarDisponibilidadUseCase.class);
    private final CatalogoEventListener listener = new CatalogoEventListener(useCase);

    @Test
    void stockAgotadoConElFormatoDeInventario() {
        // Arrange: ms-inventario envía los datos dentro de "data"
        UUID prendaId = UUID.randomUUID();
        Map<String, Object> payload = Map.of("eventType", "inventario.stock.agotado",
                "data", Map.of("prendaId", prendaId.toString(), "talla", "M"));

        // Act
        listener.onStockAgotado(payload);

        // Assert
        verify(useCase).marcarAgotada(prendaId);
    }

    @Test
    void suscripcionSuspendidaConAlmacenIdOSedeId() {
        UUID almacenId = UUID.randomUUID();
        UUID sedeId = UUID.randomUUID();

        listener.onSuscripcionSuspendida(Map.of("almacenId", almacenId.toString()));
        listener.onSuscripcionSuspendida(Map.of("data", Map.of("sedeId", sedeId.toString())));

        verify(useCase).suspenderAlmacen(almacenId);
        verify(useCase).suspenderAlmacen(sedeId);
    }

    @Test
    void ignoraEventosSinIdValido() {
        listener.onStockAgotado(Map.of("prendaId", "no-es-uuid"));
        listener.onStockAgotado(null);
        listener.onSuscripcionSuspendida(Map.of("otro", "dato"));

        verify(useCase, never()).marcarAgotada(any());
        verify(useCase, never()).suspenderAlmacen(any());
    }
}
