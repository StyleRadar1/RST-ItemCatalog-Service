package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.SincronizarAlmacenesUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.TiendasPort;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class SincronizarAlmacenesUseCaseImplTest {

    private final AlmacenRepositoryPort almacenes = mock(AlmacenRepositoryPort.class);
    private final TiendasPort tiendas = mock(TiendasPort.class);
    private final SincronizarAlmacenesUseCaseImpl useCase = new SincronizarAlmacenesUseCaseImpl(almacenes, tiendas);

    @Test
    void guardaLosAlmacenesYConservaLaSuspension() {
        UUID nuevo = UUID.randomUUID();
        UUID suspendido = UUID.randomUUID();
        when(almacenes.findById(nuevo)).thenReturn(Optional.empty());
        when(almacenes.findById(suspendido))
                .thenReturn(Optional.of(new AlmacenResumen(suspendido, "Viejo", BigDecimal.ONE, false)));
        when(tiendas.almacenes()).thenReturn(List.of(
                new TiendasPort.AlmacenTienda(nuevo, " Atelier ", new BigDecimal("4.80")),
                new TiendasPort.AlmacenTienda(suspendido, "Calle 85", null),
                new TiendasPort.AlmacenTienda(null, "Sin NIT", BigDecimal.ONE),
                new TiendasPort.AlmacenTienda(UUID.randomUUID(), " ", BigDecimal.ONE)));

        SincronizarAlmacenesUseCase.CargaAlmacenes carga = useCase.cargarDesdeTiendas();

        assertThat(carga).isEqualTo(new SincronizarAlmacenesUseCase.CargaAlmacenes(2, 2));
        verify(almacenes).save(new AlmacenResumen(nuevo, "Atelier", new BigDecimal("4.80"), true));
        verify(almacenes).save(new AlmacenResumen(suspendido, "Calle 85", BigDecimal.ZERO, false));
    }

    @Test
    void buscaElAlmacenDelUsuario() {
        UUID almacen = UUID.randomUUID();
        when(tiendas.almacenDelUsuario(10L)).thenReturn(Optional.of(almacen));

        assertThat(useCase.almacenDelUsuario(10L)).contains(almacen);
    }

    @Test
    void sinUsuarioNoConsultaTiendas() {
        assertThat(useCase.almacenDelUsuario(null)).isEmpty();
        verifyNoInteractions(tiendas);
    }

    @Test
    void registraAlUsuarioComoAdministradorDelAlmacen() {
        UUID almacen = UUID.randomUUID();
        when(tiendas.almacenDelUsuario(10L)).thenReturn(Optional.of(almacen));
        when(almacenes.findById(almacen)).thenReturn(Optional.of(new AlmacenResumen(almacen, "Atelier", BigDecimal.ONE, true)));

        assertThat(useCase.almacenDelUsuario(10L)).contains(almacen);

        verify(almacenes).save(new AlmacenResumen(almacen, "Atelier", BigDecimal.ONE, true, 10L));
    }
}
