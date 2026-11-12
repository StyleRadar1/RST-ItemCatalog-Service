package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.event.ModeracionCambiadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.event.PrendaPublicadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.GrupoPendientes;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.EventPublisherPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static edu.dosw.styleradar.itemcatalogo.application.usecase.PrendasDePrueba.prenda;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ModerarPublicacionUseCaseImplTest {

    private final PrendaRepositoryPort prendas = mock(PrendaRepositoryPort.class);
    private final AlmacenRepositoryPort almacenes = mock(AlmacenRepositoryPort.class);
    private final EventPublisherPort eventos = mock(EventPublisherPort.class);
    private final ModerarPublicacionUseCaseImpl useCase = new ModerarPublicacionUseCaseImpl(prendas, almacenes, eventos);
    private final UUID moderador = UUID.randomUUID();

    private Prenda pendienteGuardada() {
        Prenda prenda = prenda(UUID.randomUUID(), "Chaqueta", EstadoPublicacion.PENDIENTE, LocalDateTime.now());
        when(prendas.findById(prenda.getId())).thenReturn(Optional.of(prenda));
        when(prendas.save(any())).thenAnswer(inv -> inv.getArgument(0));
        return prenda;
    }

    @Test
    void apruebaUnaPublicacionYAvisaQueQuedoPublicada() {
        Prenda prenda = pendienteGuardada();

        assertThat(useCase.moderar(prenda.getId(), EstadoPublicacion.APROBADA, null, moderador).getEstado())
                .isEqualTo(EstadoPublicacion.APROBADA);
        verify(eventos).publicar(any(ModeracionCambiadaEvent.class));
        verify(eventos).publicar(any(PrendaPublicadaEvent.class));
    }

    @Test
    void rechazaUnaPublicacionConMotivoSinPublicarla() {
        Prenda prenda = pendienteGuardada();

        assertThat(useCase.moderar(prenda.getId(), EstadoPublicacion.RECHAZADA, "Foto borrosa", moderador).getEstado())
                .isEqualTo(EstadoPublicacion.RECHAZADA);
        verify(eventos).publicar(any(ModeracionCambiadaEvent.class));
        verify(eventos, never()).publicar(any(PrendaPublicadaEvent.class));
    }

    @Test
    void laDecisionDebeSerAprobarORechazar() {
        Prenda prenda = pendienteGuardada();

        assertThatThrownBy(() -> useCase.moderar(prenda.getId(), EstadoPublicacion.RETIRADA, null, moderador))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void moderarUnaPrendaInexistenteLanzaError() {
        UUID id = UUID.randomUUID();
        when(prendas.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.moderar(id, EstadoPublicacion.APROBADA, null, moderador))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void agrupaLasPendientesPorAlmacenDeMayorAMenor() {
        UUID almacenA = UUID.randomUUID();
        UUID almacenB = UUID.randomUUID();
        LocalDateTime hoy = LocalDateTime.now();
        when(almacenes.findAll()).thenReturn(List.of(
                new AlmacenResumen(almacenA, "Calle 85 Store", new BigDecimal("4.20"), true),
                new AlmacenResumen(almacenB, "Nora Vintage", new BigDecimal("3.90"), true)));
        when(prendas.findByEstado(EstadoPublicacion.PENDIENTE)).thenReturn(List.of(
                prenda(almacenB, "Blazer", EstadoPublicacion.PENDIENTE, hoy),
                prenda(almacenA, "Chaqueta", EstadoPublicacion.PENDIENTE, hoy),
                prenda(almacenA, "Buzo", EstadoPublicacion.PENDIENTE, hoy)));

        List<GrupoPendientes> grupos = useCase.pendientesPorGrupo();

        assertThat(grupos).extracting(GrupoPendientes::almacen).containsExactly("Calle 85 Store", "Nora Vintage");
        assertThat(grupos).extracting(GrupoPendientes::total).containsExactly(2, 1);
    }

    @Test
    void apruebaTodasLasPendientesDeUnAlmacen() {
        UUID almacen = UUID.randomUUID();
        LocalDateTime hoy = LocalDateTime.now();
        when(prendas.findByAlmacenId(almacen)).thenReturn(List.of(
                prenda(almacen, "Chaqueta", EstadoPublicacion.PENDIENTE, hoy),
                prenda(almacen, "Buzo", EstadoPublicacion.PENDIENTE, hoy),
                prenda(almacen, "Blazer", EstadoPublicacion.APROBADA, hoy)));
        when(prendas.save(any())).thenAnswer(inv -> inv.getArgument(0));

        List<Prenda> moderadas = useCase.moderarGrupo(almacen, EstadoPublicacion.APROBADA, null, moderador);

        assertThat(moderadas).hasSize(2).allMatch(p -> p.getEstado() == EstadoPublicacion.APROBADA);
    }

    @Test
    void rechazarUnGrupoSinMotivoNoGuardaNada() {
        UUID almacen = UUID.randomUUID();
        when(prendas.findByAlmacenId(almacen)).thenReturn(List.of(
                prenda(almacen, "Chaqueta", EstadoPublicacion.PENDIENTE, LocalDateTime.now())));

        assertThatThrownBy(() -> useCase.moderarGrupo(almacen, EstadoPublicacion.RECHAZADA, " ", moderador))
                .isInstanceOf(DomainException.class);
        verify(prendas, never()).save(any());
    }

    @Test
    void moderarUnGrupoSinPendientesLanzaError() {
        UUID almacen = UUID.randomUUID();
        when(prendas.findByAlmacenId(almacen)).thenReturn(List.of());

        assertThatThrownBy(() -> useCase.moderarGrupo(almacen, EstadoPublicacion.APROBADA, null, moderador))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
