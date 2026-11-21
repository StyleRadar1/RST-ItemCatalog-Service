package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.scheduler;

import edu.dosw.styleradar.itemcatalogo.domain.port.in.SincronizarAlmacenesUseCase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CargaAlmacenesSchedulerTest {

    private final SincronizarAlmacenesUseCase useCase = mock(SincronizarAlmacenesUseCase.class);
    private final CargaAlmacenesScheduler scheduler = new CargaAlmacenesScheduler(useCase);

    @Test
    void ejecutaLaCarga() {
        when(useCase.cargarDesdeTiendas()).thenReturn(new SincronizarAlmacenesUseCase.CargaAlmacenes(3, 0));

        scheduler.cargar();

        verify(useCase).cargarDesdeTiendas();
    }

    @Test
    void noFallaSiTiendasNoEstaDisponible() {
        when(useCase.cargarDesdeTiendas()).thenThrow(new IllegalStateException("ms-tiendas no responde"));

        assertThatCode(scheduler::cargar).doesNotThrowAnyException();
    }
}
