package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ConsultarNitAlmacenUseCaseImplTest {

    private final AlmacenRepositoryPort almacenes = mock(AlmacenRepositoryPort.class);
    private final ConsultarNitAlmacenUseCaseImpl useCase = new ConsultarNitAlmacenUseCaseImpl(almacenes);

    @Test
    void devuelveElNitDeLosAlmacenesQueLoTienen() {
        UUID conNit = UUID.randomUUID();
        UUID sinNit = UUID.randomUUID();
        when(almacenes.findAllById(any())).thenReturn(List.of(
                new AlmacenResumen(conNit, "Almacén Pruebas QA", BigDecimal.ONE, true, null, "900555777-1"),
                new AlmacenResumen(sinNit, "Sin NIT", BigDecimal.ONE, true)));

        assertThat(useCase.nitsDe(Arrays.asList(conNit, sinNit, conNit, null)))
                .containsExactlyEntriesOf(java.util.Map.of(conNit, "900555777-1"));
    }

    @Test
    void sinAlmacenesNoConsultaElRepositorio() {
        assertThat(useCase.nitsDe(List.of())).isEmpty();
        assertThat(useCase.nitsDe(null)).isEmpty();
        verifyNoInteractions(almacenes);
    }
}
