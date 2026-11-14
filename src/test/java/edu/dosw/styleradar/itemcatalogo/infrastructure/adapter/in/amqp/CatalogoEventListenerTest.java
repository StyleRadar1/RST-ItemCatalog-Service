package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.amqp;

import edu.dosw.styleradar.itemcatalogo.domain.port.in.ActualizarDisponibilidadUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class CatalogoEventListenerTest {

    private final ActualizarDisponibilidadUseCase useCase = mock(ActualizarDisponibilidadUseCase.class);
    private final CatalogoEventListener listener = new CatalogoEventListener(useCase);

    @Test
    void marcaAgotadaCuandoElStockLlegaACero() {
        UUID prendaId = UUID.randomUUID();

        listener.onInventarioActualizado(Map.of("itemId", prendaId.toString(), "stockActual", 0));

        verify(useCase).marcarAgotada(prendaId);
    }

    @Test
    void aceptaElFormatoConDatosDentroDeData() {
        UUID prendaId = UUID.randomUUID();

        listener.onInventarioActualizado(Map.of("data", Map.of("prendaId", prendaId.toString(), "estado", "AGOTADA")));

        verify(useCase).marcarAgotada(prendaId);
    }

    @Test
    void noHaceNadaSiAunQuedanUnidades() {
        listener.onInventarioActualizado(Map.of("itemId", UUID.randomUUID().toString(), "stockActual", 4));

        verify(useCase, never()).marcarAgotada(any());
    }

    @Test
    void suspendeElAlmacenConAlmacenIdTiendaIdOSedeId() {
        UUID almacenId = UUID.randomUUID();
        UUID tiendaId = UUID.randomUUID();
        UUID sedeId = UUID.randomUUID();

        listener.onSuscripcionSuspendida(Map.of("almacenId", almacenId.toString()));
        listener.onSuscripcionSuspendida(Map.of("tiendaId", tiendaId.toString()));
        listener.onSuscripcionSuspendida(Map.of("data", Map.of("sedeId", sedeId.toString())));

        verify(useCase).suspenderAlmacen(almacenId);
        verify(useCase).suspenderAlmacen(tiendaId);
        verify(useCase).suspenderAlmacen(sedeId);
    }

    @Test
    void rechazaLosMensajesInvalidosParaQueVayanALaColaDeErrores() {
        Map<String, Object> sinUuid = Map.of("itemId", "no-es-uuid");
        Map<String, Object> sinAlmacen = Map.of("otro", "dato");

        assertThatThrownBy(() -> listener.onInventarioActualizado(sinUuid))
                .isInstanceOf(AmqpRejectAndDontRequeueException.class);
        assertThatThrownBy(() -> listener.onInventarioActualizado(null))
                .isInstanceOf(AmqpRejectAndDontRequeueException.class);
        assertThatThrownBy(() -> listener.onSuscripcionSuspendida(sinAlmacen))
                .isInstanceOf(AmqpRejectAndDontRequeueException.class);
    }
}
