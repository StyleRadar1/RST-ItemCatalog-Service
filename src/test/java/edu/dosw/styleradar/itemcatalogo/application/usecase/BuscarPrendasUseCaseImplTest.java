package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.FiltroBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Ordenamiento;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static edu.dosw.styleradar.itemcatalogo.application.usecase.PrendasDePrueba.almacen;
import static edu.dosw.styleradar.itemcatalogo.application.usecase.PrendasDePrueba.aprobada;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BuscarPrendasUseCaseImplTest {

    private final PrendaRepositoryPort prendas = mock(PrendaRepositoryPort.class);
    private final AlmacenRepositoryPort almacenes = mock(AlmacenRepositoryPort.class);
    private final BuscarPrendasUseCaseImpl useCase = new BuscarPrendasUseCaseImpl(prendas, almacenes);

    private final AlmacenResumen buenaReputacion = almacen("4.80", true);
    private final AlmacenResumen bajaReputacion = almacen("3.10", true);
    private final AlmacenResumen suspendido = almacen("5.00", false);
    private final LocalDateTime hoy = LocalDateTime.now();

    private final Prenda antigua = aprobada(buenaReputacion.almacenId(), "Chaqueta", hoy.minusDays(5));
    private final Prenda reciente = aprobada(bajaReputacion.almacenId(), "Chaqueta", hoy);
    private final Prenda deSuspendido = aprobada(suspendido.almacenId(), "Chaqueta", hoy);

    @BeforeEach
    void setUp() {
        when(almacenes.findAll()).thenReturn(List.of(buenaReputacion, bajaReputacion, suspendido));
        when(prendas.findByEstado(EstadoPublicacion.APROBADA)).thenReturn(List.of(antigua, reciente, deSuspendido));
    }

    @Test
    void porDefectoOrdenaPorRecientesYOcultaAlmacenesSuspendidos() {
        List<Prenda> resultado = useCase.buscar(new FiltroBusqueda(null, null, null, null, null, null, null, null));

        assertThat(resultado).containsExactly(reciente, antigua);
    }

    @Test
    void ordenaPorReputacionDelAlmacen() {
        List<Prenda> resultado = useCase.buscar(new FiltroBusqueda(null, null, null, null, null, null, null, Ordenamiento.REPUTACION));

        assertThat(resultado).containsExactly(antigua, reciente);
    }

    @Test
    void aplicaElFiltro() {
        List<Prenda> resultado = useCase.buscar(new FiltroBusqueda(null, "Vestido", null, null, null, null, null, null));

        assertThat(resultado).isEmpty();
    }

    @Test
    void devuelvePrendasSimilares() {
        when(prendas.findById(antigua.getId())).thenReturn(Optional.of(antigua));

        assertThat(useCase.similares(antigua.getId())).containsExactly(reciente);
    }

    @Test
    void similaresDeUnaPrendaInexistenteLanzaError() {
        UUID id = UUID.randomUUID();
        when(prendas.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.similares(id)).isInstanceOf(ResourceNotFoundException.class);
    }
}
