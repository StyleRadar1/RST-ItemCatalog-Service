package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static edu.dosw.styleradar.itemcatalogo.application.usecase.PrendasDePrueba.almacen;
import static edu.dosw.styleradar.itemcatalogo.application.usecase.PrendasDePrueba.aprobada;
import static edu.dosw.styleradar.itemcatalogo.application.usecase.PrendasDePrueba.prenda;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsultarCatalogoAlmacenUseCaseImplTest {

    private final PrendaRepositoryPort prendas = mock(PrendaRepositoryPort.class);
    private final AlmacenRepositoryPort almacenes = mock(AlmacenRepositoryPort.class);
    private final ConsultarCatalogoAlmacenUseCaseImpl useCase = new ConsultarCatalogoAlmacenUseCaseImpl(prendas, almacenes);

    private final AlmacenResumen almacen = almacen("4.50", true);
    private final LocalDateTime hoy = LocalDateTime.now();
    private final Prenda nueva = aprobada(almacen.almacenId(), "Chaqueta", hoy);
    private final Prenda vieja = aprobada(almacen.almacenId(), "Buzo", hoy.minusDays(10));
    private final Prenda pendiente = prenda(almacen.almacenId(), "Blazer", EstadoPublicacion.PENDIENTE, hoy);

    @Test
    void elCatalogoSoloTienePrendasVisiblesDeLaMasReciente() {
        when(almacenes.findById(almacen.almacenId())).thenReturn(Optional.of(almacen));
        when(prendas.findByAlmacenId(almacen.almacenId())).thenReturn(List.of(vieja, pendiente, nueva));

        assertThat(useCase.catalogo(almacen.almacenId())).containsExactly(nueva, vieja);
    }

    @Test
    void lasNovedadesSonDeLosUltimosSieteDias() {
        when(almacenes.findById(almacen.almacenId())).thenReturn(Optional.of(almacen));
        when(prendas.findByAlmacenId(almacen.almacenId())).thenReturn(List.of(vieja, nueva));

        assertThat(useCase.novedades(almacen.almacenId())).containsExactly(nueva);
    }

    @Test
    void unAlmacenSuspendidoNoMuestraCatalogo() {
        AlmacenResumen suspendido = almacen("4.00", false);
        when(almacenes.findById(suspendido.almacenId())).thenReturn(Optional.of(suspendido));

        assertThat(useCase.catalogo(suspendido.almacenId())).isEmpty();
    }

    @Test
    void unAlmacenInexistenteLanzaError() {
        UUID id = UUID.randomUUID();
        when(almacenes.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.catalogo(id)).isInstanceOf(ResourceNotFoundException.class);
    }
}
