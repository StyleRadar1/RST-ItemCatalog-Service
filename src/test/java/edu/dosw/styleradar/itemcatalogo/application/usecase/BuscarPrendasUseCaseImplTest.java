package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.ParametroInvalidoException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.FiltroBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Ordenamiento;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.PrendaEncontrada;
import edu.dosw.styleradar.itemcatalogo.domain.model.Ubicacion;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.DistanciaAlmacenPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.TallaDisponiblePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static edu.dosw.styleradar.itemcatalogo.application.usecase.PrendasDePrueba.almacen;
import static edu.dosw.styleradar.itemcatalogo.application.usecase.PrendasDePrueba.aprobada;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class BuscarPrendasUseCaseImplTest {

    private final PrendaRepositoryPort prendas = mock(PrendaRepositoryPort.class);
    private final AlmacenRepositoryPort almacenes = mock(AlmacenRepositoryPort.class);
    private final DistanciaAlmacenPort distancias = mock(DistanciaAlmacenPort.class);
    private final TallaDisponiblePort tallas = mock(TallaDisponiblePort.class);
    private final BuscarPrendasUseCaseImpl useCase = new BuscarPrendasUseCaseImpl(prendas, almacenes, distancias, tallas);
    private final Ubicacion usuario = new Ubicacion(4.6683, -74.0546);

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
        List<Prenda> resultado = buscar(new FiltroBusqueda(null, null, null, null, null, null, null, null, null, null));

        assertThat(resultado).containsExactly(reciente, antigua);
    }

    @Test
    void ordenaPorReputacionDelAlmacen() {
        List<Prenda> resultado = buscar(new FiltroBusqueda(null, null, null, null, null, null, null, null, Ordenamiento.REPUTACION, null));

        assertThat(resultado).containsExactly(antigua, reciente);
    }

    @Test
    void aplicaElFiltro() {
        List<Prenda> resultado = buscar(new FiltroBusqueda(null, "Vestido", null, null, null, null, null, null, null, null));

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

    @Test
    void ordenaPorDistanciaConsultandoUnaVezCadaAlmacen() {
        Prenda otraDelAlmacenCercano = aprobada(bajaReputacion.almacenId(), "Buzo", hoy.minusDays(1));
        when(prendas.findByEstado(EstadoPublicacion.APROBADA)).thenReturn(List.of(antigua, reciente, otraDelAlmacenCercano));
        when(distancias.kilometros(buenaReputacion.almacenId(), usuario)).thenReturn(3.5);
        when(distancias.kilometros(bajaReputacion.almacenId(), usuario)).thenReturn(0.4);

        List<Prenda> resultado = buscar(
                new FiltroBusqueda(null, null, null, null, null, null, null, null, Ordenamiento.DISTANCIA, usuario));

        assertThat(resultado).containsExactly(reciente, otraDelAlmacenCercano, antigua);
        verify(distancias, times(1)).kilometros(bajaReputacion.almacenId(), usuario);
    }

    @Test
    void conUbicacionDevuelveLaDistanciaDeCadaPrenda() {
        when(distancias.kilometros(buenaReputacion.almacenId(), usuario)).thenReturn(3.5);
        when(distancias.kilometros(bajaReputacion.almacenId(), usuario)).thenReturn(0.4);

        List<PrendaEncontrada> resultado = useCase.buscar(
                new FiltroBusqueda(null, null, null, null, null, null, null, null, null, usuario));

        assertThat(resultado).extracting(PrendaEncontrada::prenda).containsExactly(reciente, antigua);
        assertThat(resultado).extracting(PrendaEncontrada::distanciaKm).containsExactly(0.4, 3.5);
    }

    @Test
    void sinUbicacionNoConsultaDistancias() {
        List<PrendaEncontrada> resultado = useCase.buscar(
                new FiltroBusqueda(null, null, null, null, null, null, null, null, null, null));

        assertThat(resultado).extracting(PrendaEncontrada::distanciaKm).containsOnlyNulls();
        verifyNoInteractions(distancias);
    }

    @Test
    void ordenarPorDistanciaSinUbicacionLanzaError() {
        FiltroBusqueda filtro = new FiltroBusqueda(null, null, null, null, null, null, null, null, Ordenamiento.DISTANCIA, null);

        assertThatThrownBy(() -> useCase.buscar(filtro)).isInstanceOf(ParametroInvalidoException.class);
    }

    @Test
    void unPrecioNegativoLanzaErrorConElCampo() {
        FiltroBusqueda filtro = new FiltroBusqueda(null, null, null, null, null, null,
                new BigDecimal("-1"), new BigDecimal("-5"), null, null);

        assertThatThrownBy(() -> useCase.buscar(filtro))
                .isInstanceOf(ParametroInvalidoException.class)
                .satisfies(ex -> assertThat(((ParametroInvalidoException) ex).getErrores())
                        .containsEntry("precioMin", "El precio mínimo no puede ser negativo")
                        .containsEntry("precioMax", "El precio máximo no puede ser negativo"));
        verifyNoInteractions(prendas);
    }

    @Test
    void losFiltrosDebenUsarLosValoresUnificados() {
        FiltroBusqueda filtro = new FiltroBusqueda(null, "Gorra", "Rojo", "m".repeat(61), "Elegante", "XXXL",
                null, null, null, new Ubicacion(95, -200));

        assertThatThrownBy(() -> useCase.buscar(filtro))
                .isInstanceOf(ParametroInvalidoException.class)
                .satisfies(ex -> assertThat(((ParametroInvalidoException) ex).getErrores())
                        .containsOnlyKeys("tipo", "color", "marca", "estilo", "talla", "lat", "lng"));
        verifyNoInteractions(prendas);
    }

    @Test
    void unColorMuyLargoAvisaElLimite() {
        FiltroBusqueda filtro = new FiltroBusqueda(null, null, "a".repeat(41), null, null, null, null, null, null, null);

        assertThatThrownBy(() -> useCase.buscar(filtro))
                .satisfies(ex -> assertThat(((ParametroInvalidoException) ex).getErrores())
                        .containsEntry("color", "El color no puede superar los 40 caracteres"));
    }

    @Test
    void aceptaLosValoresSinImportarMayusculasNiTildes() {
        when(tallas.prendasConStock("42")).thenReturn(Set.of());

        assertThat(useCase.buscar(new FiltroBusqueda(null, "sueter", "cafe", "luna store", "urbano", "42",
                null, null, null, null))).isEmpty();
    }

    @Test
    void unMinimoMayorQueElMaximoLanzaError() {
        FiltroBusqueda filtro = new FiltroBusqueda(null, null, null, null, null, null,
                new BigDecimal("200000"), new BigDecimal("100000"), null, null);

        assertThatThrownBy(() -> useCase.buscar(filtro))
                .isInstanceOf(ParametroInvalidoException.class)
                .hasMessage("El precio mínimo no puede ser mayor que el precio máximo");
    }

    @Test
    void filtraPorTallaSoloLasPrendasConStockEnInventario() {
        when(tallas.prendasConStock("M")).thenReturn(Set.of(antigua.getId(), deSuspendido.getId()));

        List<Prenda> resultado = buscar(new FiltroBusqueda(null, null, null, null, null, "M", null, null, null, null));

        assertThat(resultado).containsExactly(antigua);
        verify(tallas, times(1)).prendasConStock("M");
    }

    private List<Prenda> buscar(FiltroBusqueda filtro) {
        return useCase.buscar(filtro).stream().map(PrendaEncontrada::prenda).toList();
    }

    @Test
    void sinTallaNoConsultaElInventario() {
        useCase.buscar(new FiltroBusqueda(null, null, null, null, null, null, null, null, null, null));

        verifyNoInteractions(tallas);
    }
}
