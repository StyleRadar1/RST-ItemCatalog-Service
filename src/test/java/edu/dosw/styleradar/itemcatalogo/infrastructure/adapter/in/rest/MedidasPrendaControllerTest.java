package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.MedidasPrendaRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.MedidasPrendaResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.TallasMedidasResponse;
import edu.dosw.styleradar.itemcatalogo.domain.model.GuiaMedidas;
import edu.dosw.styleradar.itemcatalogo.domain.model.MedidaPrenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Solicitante;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarDisponibilidadUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.GestionarMedidasPrendaUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.SincronizarAlmacenesUseCase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MedidasPrendaControllerTest {

    private final GestionarMedidasPrendaUseCase useCase = mock(GestionarMedidasPrendaUseCase.class);
    private final SincronizarAlmacenesUseCase almacenes = mock(SincronizarAlmacenesUseCase.class);
    private final MedidasPrendaController controller = new MedidasPrendaController(useCase, almacenes);
    private final UUID prendaId = UUID.randomUUID();
    private final UUID almacenId = UUID.randomUUID();
    private final List<MedidaPrenda> tabla = List.of(new MedidaPrenda("M", 92.0, 100.0, 74.0, 82.0, null, null, 70.0));
    private final MedidasPrendaResponse.MedidaTalla esperada =
            new MedidasPrendaResponse.MedidaTalla("M", 92.0, 100.0, 74.0, 82.0, null, null, 70.0);

    @Test
    void registraLasMedidasConElAlmacenDelUsuario() {
        when(almacenes.almacenDelUsuario(10L)).thenReturn(Optional.of(almacenId));
        when(useCase.registrar(prendaId, new Solicitante(almacenId, false), tabla)).thenReturn(tabla);
        MedidasPrendaRequest request = new MedidasPrendaRequest(List.of(
                new MedidasPrendaRequest.MedidaTalla("M", 92.0, 100.0, 74.0, 82.0, null, null, 70.0)));

        MedidasPrendaResponse respuesta = controller.registrar(prendaId, null, "STORE_ADMIN", null, "10", request).getBody();

        assertThat(respuesta.garmentId()).isEqualTo(prendaId);
        assertThat(respuesta.sizes()).containsExactly(esperada);
    }

    @Test
    void consultaLasMedidas() {
        when(useCase.consultar(prendaId)).thenReturn(tabla);

        assertThat(controller.consultar(prendaId).getBody().sizes()).containsExactly(esperada);
    }

    @Test
    void laGuiaDeTallasDevuelveElLargo() {
        ConsultarDisponibilidadUseCase disponibilidad = mock(ConsultarDisponibilidadUseCase.class);
        when(disponibilidad.tallasYMedidas(prendaId)).thenReturn(new ConsultarDisponibilidadUseCase.TallasYMedidas(List.of("M"),
                List.of(new GuiaMedidas.MedidasTalla("M", 92.0, 100.0, 74.0, 82.0, null, null, 70.0))));

        assertThat(new DisponibilidadController(disponibilidad).tallasMedidas(prendaId).getBody().sizes())
                .containsExactly(new TallasMedidasResponse.RangoMedidas("M", 92.0, 100.0, 74.0, 82.0, null, null, 70.0));
    }
}
