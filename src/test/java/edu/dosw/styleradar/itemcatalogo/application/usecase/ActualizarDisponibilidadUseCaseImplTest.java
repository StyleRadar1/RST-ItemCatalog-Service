package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static edu.dosw.styleradar.itemcatalogo.application.usecase.PrendasDePrueba.almacen;
import static edu.dosw.styleradar.itemcatalogo.application.usecase.PrendasDePrueba.aprobada;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ActualizarDisponibilidadUseCaseImplTest {

    private final PrendaRepositoryPort prendas = mock(PrendaRepositoryPort.class);
    private final AlmacenRepositoryPort almacenes = mock(AlmacenRepositoryPort.class);
    private final ActualizarDisponibilidadUseCaseImpl useCase = new ActualizarDisponibilidadUseCaseImpl(prendas, almacenes);

    @Test
    void marcaLaPrendaComoAgotada() {
        Prenda prenda = aprobada(UUID.randomUUID(), "Chaqueta", LocalDateTime.now());
        when(prendas.findById(prenda.getId())).thenReturn(Optional.of(prenda));

        useCase.marcarAgotada(prenda.getId());

        assertThat(prenda.isDisponible()).isFalse();
        verify(prendas).save(prenda);
    }

    @Test
    void suspendeElAlmacen() {
        AlmacenResumen almacen = almacen("4.50", true);
        when(almacenes.findById(almacen.almacenId())).thenReturn(Optional.of(almacen));

        useCase.suspenderAlmacen(almacen.almacenId());

        verify(almacenes).save(almacen.suspender());
    }

    @Test
    void ignoraPrendasYAlmacenesQueNoEstanEnElCatalogo() {
        when(prendas.findById(any())).thenReturn(Optional.empty());
        when(almacenes.findById(any())).thenReturn(Optional.empty());

        useCase.marcarAgotada(UUID.randomUUID());
        useCase.suspenderAlmacen(UUID.randomUUID());

        verify(prendas, never()).save(any());
        verify(almacenes, never()).save(any());
    }
}
