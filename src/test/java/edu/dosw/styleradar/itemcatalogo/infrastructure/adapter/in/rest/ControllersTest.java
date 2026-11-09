package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.ModeracionRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.request.PrendaRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.CatalogoResumenResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import edu.dosw.styleradar.itemcatalogo.domain.model.AtributosBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.Fotografia;
import edu.dosw.styleradar.itemcatalogo.domain.model.Precio;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Solicitante;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.BuscarPrendasUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarCatalogoAlmacenUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.GestionarPrendaUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ModerarPublicacionUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllersTest {

    private final UUID almacenId = UUID.randomUUID();
    private final Prenda prenda = Prenda.publicar(almacenId, "Chaqueta denim",
            new AtributosBusqueda("Chaqueta", "Azul", "Levis", "Urbano"), new Precio(new BigDecimal("190000")),
            List.of(new Fotografia("https://foto/1.jpg", 1)));
    private final PrendaRequest request = new PrendaRequest(almacenId, "Chaqueta denim", "Chaqueta", "Azul", "Levis",
            "Urbano", new BigDecimal("190000"), List.of("https://foto/1.jpg"));

    @Test
    void buscaYConsultaSimilares() {
        BuscarPrendasUseCase useCase = mock(BuscarPrendasUseCase.class);
        when(useCase.buscar(any())).thenReturn(List.of(prenda));
        when(useCase.similares(prenda.getId())).thenReturn(List.of());
        BusquedaPrendaController controller = new BusquedaPrendaController(useCase);

        ResponseEntity<List<PrendaResponse>> busqueda = controller.buscar("denim", null, null, null, null, null, null, null);

        assertThat(busqueda.getBody()).extracting(PrendaResponse::nombre).containsExactly("Chaqueta denim");
        assertThat(controller.similares(prenda.getId()).getBody()).isEmpty();
    }

    @Test
    void consultaElCatalogoDelAlmacen() {
        ConsultarCatalogoAlmacenUseCase useCase = mock(ConsultarCatalogoAlmacenUseCase.class);
        when(useCase.catalogo(almacenId)).thenReturn(List.of(prenda));
        when(useCase.novedades(almacenId)).thenReturn(List.of(prenda));
        CatalogoAlmacenController controller = new CatalogoAlmacenController(useCase);

        assertThat(controller.catalogo(almacenId).getBody()).hasSize(1);
        assertThat(controller.novedades(almacenId).getBody()).hasSize(1);
        CatalogoResumenResponse resumen = controller.resumen(almacenId).getBody();
        assertThat(resumen.totalPrendas()).isEqualTo(1);
        assertThat(resumen.prendas()).hasSize(1);
    }

    @Test
    void gestionaUnaPrenda() {
        GestionarPrendaUseCase useCase = mock(GestionarPrendaUseCase.class);
        when(useCase.publicar(eq(almacenId), eq("Chaqueta denim"), any(), any(), any())).thenReturn(prenda);
        when(useCase.obtener(prenda.getId())).thenReturn(prenda);
        when(useCase.editar(eq(prenda.getId()), eq(new Solicitante(almacenId, false)), any(), any(), any(), any())).thenReturn(prenda);
        when(useCase.retirar(prenda.getId(), new Solicitante(null, true))).thenReturn(prenda);
        PrendaController controller = new PrendaController(useCase);

        assertThat(controller.publicar(request).getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(controller.obtener(prenda.getId()).getBody().id()).isEqualTo(prenda.getId());
        assertThat(controller.editar(prenda.getId(), almacenId, null, request).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(controller.retirar(prenda.getId(), null, "admin").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void moderaPublicaciones() {
        ModerarPublicacionUseCase useCase = mock(ModerarPublicacionUseCase.class);
        UUID moderador = UUID.randomUUID();
        when(useCase.pendientesPorAlmacen()).thenReturn(Map.of(almacenId, List.of(prenda)));
        when(useCase.moderar(prenda.getId(), EstadoPublicacion.APROBADA, null, moderador)).thenReturn(prenda);
        ModeracionController controller = new ModeracionController(useCase);

        assertThat(controller.pendientes().getBody()).containsKey(almacenId);
        assertThat(controller.moderar(prenda.getId(), new ModeracionRequest(EstadoPublicacion.APROBADA, null, moderador))
                .getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void sinPendientesDevuelveMapaVacio() {
        ModerarPublicacionUseCase useCase = mock(ModerarPublicacionUseCase.class);
        when(useCase.pendientesPorAlmacen()).thenReturn(Collections.emptyMap());

        assertThat(new ModeracionController(useCase).pendientes().getBody()).isEmpty();
    }
}
