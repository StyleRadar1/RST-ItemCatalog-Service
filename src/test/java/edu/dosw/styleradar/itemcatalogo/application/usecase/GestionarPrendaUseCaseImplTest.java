package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.event.NuevaPrendaCreadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.event.PrendaEliminadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.exception.AccesoDenegadoException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.AtributosBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.Fotografia;
import edu.dosw.styleradar.itemcatalogo.domain.model.Precio;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Solicitante;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.EventPublisherPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static edu.dosw.styleradar.itemcatalogo.application.usecase.PrendasDePrueba.almacen;
import static edu.dosw.styleradar.itemcatalogo.application.usecase.PrendasDePrueba.aprobada;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GestionarPrendaUseCaseImplTest {

    private final PrendaRepositoryPort prendas = mock(PrendaRepositoryPort.class);
    private final AlmacenRepositoryPort almacenes = mock(AlmacenRepositoryPort.class);
    private final EventPublisherPort eventos = mock(EventPublisherPort.class);
    private final GestionarPrendaUseCaseImpl useCase = new GestionarPrendaUseCaseImpl(prendas, almacenes, eventos);

    private final AtributosBusqueda atributos = new AtributosBusqueda("Chaqueta", "Azul", "Levis", "Urbano");
    private final Precio precio = new Precio(new BigDecimal("190000"));
    private final List<Fotografia> fotos = List.of(new Fotografia("https://foto/1.jpg", 1));

    @BeforeEach
    void setUp() {
        when(prendas.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void publicaEnUnAlmacenActivoYAvisaConUnEvento() {
        AlmacenResumen almacen = almacen("4.50", true);
        when(almacenes.findById(almacen.almacenId())).thenReturn(Optional.of(almacen));

        Prenda prenda = useCase.publicar(almacen.almacenId(), "Chaqueta denim", atributos, precio, fotos);

        assertThat(prenda.getEstado()).isEqualTo(EstadoPublicacion.PENDIENTE);
        verify(eventos).publicar(new NuevaPrendaCreadaEvent(prenda.getId(), almacen.almacenId(), prenda.getFechaPublicacion()));
    }

    @Test
    void noPublicaEnAlmacenSuspendidoNiInexistente() {
        AlmacenResumen suspendido = almacen("4.50", false);
        UUID inexistente = UUID.randomUUID();
        when(almacenes.findById(suspendido.almacenId())).thenReturn(Optional.of(suspendido));
        when(almacenes.findById(inexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.publicar(suspendido.almacenId(), "Chaqueta", atributos, precio, fotos))
                .isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> useCase.publicar(inexistente, "Chaqueta", atributos, precio, fotos))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void elDuenoEditaYRetiraLaPrendaYSeAvisaElRetiro() {
        Prenda prenda = aprobada(UUID.randomUUID(), "Chaqueta", LocalDateTime.now());
        Solicitante dueno = new Solicitante(prenda.getAlmacenId(), false);
        when(prendas.findById(prenda.getId())).thenReturn(Optional.of(prenda));

        assertThat(useCase.editar(prenda.getId(), dueno, "Chaqueta editada", atributos, precio, fotos).getNombre())
                .isEqualTo("Chaqueta editada");
        assertThat(useCase.retirar(prenda.getId(), dueno).getEstado()).isEqualTo(EstadoPublicacion.RETIRADA);
        verify(eventos).publicar(any(PrendaEliminadaEvent.class));
    }

    @Test
    void elAdministradorPuedeRetirarCualquierPrenda() {
        Prenda prenda = aprobada(UUID.randomUUID(), "Chaqueta", LocalDateTime.now());
        when(prendas.findById(prenda.getId())).thenReturn(Optional.of(prenda));

        assertThat(useCase.retirar(prenda.getId(), new Solicitante(null, true)).getEstado())
                .isEqualTo(EstadoPublicacion.RETIRADA);
    }

    @Test
    void otroAlmacenNoPuedeEditarNiRetirar() {
        Prenda prenda = aprobada(UUID.randomUUID(), "Chaqueta", LocalDateTime.now());
        Solicitante otro = new Solicitante(UUID.randomUUID(), false);
        when(prendas.findById(prenda.getId())).thenReturn(Optional.of(prenda));

        assertThatThrownBy(() -> useCase.editar(prenda.getId(), otro, "Otra", atributos, precio, fotos))
                .isInstanceOf(AccesoDenegadoException.class);
        assertThatThrownBy(() -> useCase.retirar(prenda.getId(), otro))
                .isInstanceOf(AccesoDenegadoException.class);
        verify(eventos, never()).publicar(any(PrendaEliminadaEvent.class));
    }

    @Test
    void obtenerUnaPrendaInexistenteLanzaError() {
        UUID id = UUID.randomUUID();
        when(prendas.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.obtener(id)).isInstanceOf(ResourceNotFoundException.class);
    }
}
