package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static edu.dosw.styleradar.itemcatalogo.application.usecase.PrendasDePrueba.prenda;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ModerarPublicacionUseCaseImplTest {

    private final PrendaRepositoryPort prendas = mock(PrendaRepositoryPort.class);
    private final ModerarPublicacionUseCaseImpl useCase = new ModerarPublicacionUseCaseImpl(prendas);
    private final UUID moderador = UUID.randomUUID();

    private Prenda pendienteGuardada() {
        Prenda prenda = prenda(UUID.randomUUID(), "Chaqueta", EstadoPublicacion.PENDIENTE, LocalDateTime.now());
        when(prendas.findById(prenda.getId())).thenReturn(Optional.of(prenda));
        when(prendas.save(any())).thenAnswer(inv -> inv.getArgument(0));
        return prenda;
    }

    @Test
    void apruebaUnaPublicacion() {
        Prenda prenda = pendienteGuardada();

        assertThat(useCase.moderar(prenda.getId(), EstadoPublicacion.APROBADA, null, moderador).getEstado())
                .isEqualTo(EstadoPublicacion.APROBADA);
    }

    @Test
    void rechazaUnaPublicacionConMotivo() {
        Prenda prenda = pendienteGuardada();

        assertThat(useCase.moderar(prenda.getId(), EstadoPublicacion.RECHAZADA, "Foto borrosa", moderador).getEstado())
                .isEqualTo(EstadoPublicacion.RECHAZADA);
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
    void agrupaLasPendientesPorAlmacen() {
        UUID almacenA = UUID.randomUUID();
        UUID almacenB = UUID.randomUUID();
        LocalDateTime hoy = LocalDateTime.now();
        when(prendas.findByEstado(EstadoPublicacion.PENDIENTE)).thenReturn(List.of(
                prenda(almacenA, "Chaqueta", EstadoPublicacion.PENDIENTE, hoy),
                prenda(almacenA, "Buzo", EstadoPublicacion.PENDIENTE, hoy),
                prenda(almacenB, "Blazer", EstadoPublicacion.PENDIENTE, hoy)));

        Map<UUID, List<Prenda>> grupos = useCase.pendientesPorAlmacen();

        assertThat(grupos.get(almacenA)).hasSize(2);
        assertThat(grupos.get(almacenB)).hasSize(1);
    }
}
