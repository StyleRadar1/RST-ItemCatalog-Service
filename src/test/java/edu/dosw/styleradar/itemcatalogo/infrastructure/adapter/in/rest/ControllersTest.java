package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.ModeracionRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.request.PrendaRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.CatalogoResumenResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PendientesResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import edu.dosw.styleradar.itemcatalogo.domain.model.AtributosBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.Fotografia;
import edu.dosw.styleradar.itemcatalogo.domain.model.GrupoPendientes;
import edu.dosw.styleradar.itemcatalogo.domain.model.IdAlmacen;
import edu.dosw.styleradar.itemcatalogo.domain.model.Precio;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.PrendaEncontrada;
import edu.dosw.styleradar.itemcatalogo.domain.model.Solicitante;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.BuscarPrendasUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarCatalogoAlmacenUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.GestionarPrendaUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ModerarPublicacionUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import edu.dosw.styleradar.itemcatalogo.domain.exception.AccesoDenegadoException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ParametroInvalidoException;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.SincronizarAlmacenesUseCase;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.DisponibilidadResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.TallasMedidasResponse;
import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;
import edu.dosw.styleradar.itemcatalogo.domain.model.GuiaMedidas;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarDisponibilidadUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarNitAlmacenUseCase;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllersTest {

    private static final String NIT = "900555777-1";
    private final UUID almacenId = UUID.randomUUID();
    private final Prenda prenda = Prenda.publicar(almacenId, "Chaqueta denim",
            new AtributosBusqueda("Chaqueta", "Azul", "Levis", "Urbano"), new Precio(new BigDecimal("190000")),
            List.of(new Fotografia("https://foto/1.jpg", 1)));
    private final ConsultarNitAlmacenUseCase nits = ids -> ids.contains(almacenId) ? Map.of(almacenId, NIT) : Map.of();
    private final PrendaRequest request = new PrendaRequest(almacenId, "Chaqueta denim", "Chaqueta", "Azul", "Levis",
            "Urbano", new BigDecimal("190000"), List.of("https://foto/1.jpg"));

    @Test
    void buscaYConsultaSimilares() {
        BuscarPrendasUseCase useCase = mock(BuscarPrendasUseCase.class);
        when(useCase.buscar(any())).thenReturn(List.of(new PrendaEncontrada(prenda, 1.5)));
        when(useCase.similares(prenda.getId())).thenReturn(List.of());
        BusquedaPrendaController controller = new BusquedaPrendaController(useCase, nits);

        ResponseEntity<List<PrendaResponse>> busqueda = controller.buscar("denim", null, null, null, null, null, null, null, null, null, null);

        assertThat(busqueda.getBody()).extracting(PrendaResponse::nombre).containsExactly("Chaqueta denim");
        assertThat(busqueda.getBody()).extracting(PrendaResponse::distanciaKm).containsExactly(1.5);
        assertThat(busqueda.getBody()).extracting(PrendaResponse::storeNit).containsExactly(NIT);
        assertThat(controller.similares(prenda.getId()).getBody()).isEmpty();
    }

    @Test
    void consultaElCatalogoDelAlmacen() {
        ConsultarCatalogoAlmacenUseCase useCase = mock(ConsultarCatalogoAlmacenUseCase.class);
        when(useCase.catalogo(almacenId)).thenReturn(List.of(prenda));
        when(useCase.novedades(almacenId)).thenReturn(List.of(prenda));
        CatalogoAlmacenController controller = new CatalogoAlmacenController(useCase, nits);

        assertThat(controller.catalogo(almacenId).getBody()).extracting(PrendaResponse::storeNit).containsExactly(NIT);
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
        SincronizarAlmacenesUseCase almacenes = mock(SincronizarAlmacenesUseCase.class);
        when(almacenes.almacenDelUsuario(any())).thenReturn(Optional.empty());
        PrendaController controller = new PrendaController(useCase, almacenes, nits);

        assertThat(controller.publicar(request, null, almacenId.toString(), "STORE_ADMIN", null, null).getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
        assertThat(controller.obtener(prenda.getId()).getBody().id()).isEqualTo(prenda.getId());
        assertThat(controller.obtener(prenda.getId()).getBody().storeNit()).isEqualTo(NIT);
        assertThat(controller.obtener(prenda.getId()).getBody().almacenId()).isEqualTo(almacenId);
        assertThat(controller.editar(prenda.getId(), null, almacenId.toString(), "STORE_ADMIN", null, null, request).getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(controller.retirar(prenda.getId(), null, null, null, "ROLE_ADMIN_STYLERADAR", null).getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void usaElAlmacenDelUsuarioEnMsTiendasYResuelveHeaderNit() {
        GestionarPrendaUseCase useCase = mock(GestionarPrendaUseCase.class);
        SincronizarAlmacenesUseCase almacenes = mock(SincronizarAlmacenesUseCase.class);
        when(almacenes.almacenDelUsuario(10L)).thenReturn(Optional.of(almacenId));
        UUID desdeNit = IdAlmacen.desdeNit(NIT);
        when(useCase.publicar(eq(almacenId), any(), any(), any(), any())).thenReturn(prenda);
        when(useCase.publicar(eq(desdeNit), any(), any(), any(), any())).thenReturn(prenda);
        when(useCase.retirar(prenda.getId(), new Solicitante(almacenId, false))).thenReturn(prenda);
        PrendaController controller = new PrendaController(useCase, almacenes, nits);
        PrendaRequest sinAlmacen = new PrendaRequest(null, request.nombre(), request.tipo(), request.color(), request.marca(),
                request.estilo(), request.precio(), request.fotografias());

        assertThat(controller.publicar(sinAlmacen, null, null, "STORE_ADMIN", null, "10").getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
        assertThat(controller.publicar(sinAlmacen, NIT, null, "ADMIN_ALMACEN", null, null).getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
        assertThat(controller.retirar(prenda.getId(), null, null, "ADMIN_ALMACEN", null, "10").getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThatThrownBy(() -> controller.publicar(sinAlmacen, null, null, "ADMIN_STYLERADAR", null, "abc"))
                .isInstanceOf(ParametroInvalidoException.class);
    }

    @Test
    void rechazaPublicacionEdicionYRetiroSinRolOAlmacenAjeno() {
        GestionarPrendaUseCase useCase = mock(GestionarPrendaUseCase.class);
        SincronizarAlmacenesUseCase almacenes = mock(SincronizarAlmacenesUseCase.class);
        when(almacenes.almacenDelUsuario(10L)).thenReturn(Optional.of(almacenId));
        PrendaController controller = new PrendaController(useCase, almacenes, nits);
        UUID otroAlmacen = UUID.randomUUID();
        PrendaRequest paraOtroAlmacen = new PrendaRequest(otroAlmacen, request.nombre(), request.tipo(), request.color(),
                "Almacén Pruebas QA", request.estilo(), request.precio(), request.fotografias());

        assertThatThrownBy(() -> controller.publicar(request, almacenId.toString(), null, "BUYER", null, null))
                .isInstanceOf(AccesoDenegadoException.class);
        assertThatThrownBy(() -> controller.publicar(request, almacenId.toString(), null, null, null, null))
                .isInstanceOf(AccesoDenegadoException.class);
        assertThatThrownBy(() -> controller.publicar(paraOtroAlmacen, almacenId.toString(), null, "STORE_ADMIN", null, "10"))
                .isInstanceOf(AccesoDenegadoException.class);
        assertThatThrownBy(() -> controller.publicar(paraOtroAlmacen, null, null, "STORE_ADMIN", null, null))
                .isInstanceOf(AccesoDenegadoException.class);
        assertThatThrownBy(() -> controller.publicar(request, otroAlmacen.toString(), null, "STORE_ADMIN", null, "10"))
                .isInstanceOf(AccesoDenegadoException.class);
        assertThatThrownBy(() -> controller.editar(prenda.getId(), almacenId.toString(), null, "BUYER", null, null, request))
                .isInstanceOf(AccesoDenegadoException.class);
        assertThatThrownBy(() -> controller.retirar(prenda.getId(), almacenId.toString(), null, null, null, null))
                .isInstanceOf(AccesoDenegadoException.class);
    }

    @Test
    void moderaPublicaciones() {
        ModerarPublicacionUseCase useCase = mock(ModerarPublicacionUseCase.class);
        UUID moderador = UUID.randomUUID();
        ModeracionRequest aprobar = new ModeracionRequest(EstadoPublicacion.APROBADA, null, moderador.toString());
        when(useCase.pendientesPorGrupo()).thenReturn(List.of(new GrupoPendientes(almacenId, "Calle 85 Store", List.of(prenda))));
        when(useCase.moderar(prenda.getId(), EstadoPublicacion.APROBADA, null, moderador)).thenReturn(prenda);
        when(useCase.moderarGrupo(almacenId, EstadoPublicacion.APROBADA, null, moderador)).thenReturn(List.of(prenda));
        ModeracionController controller = new ModeracionController(useCase, nits);

        PendientesResponse pendientes = controller.pendientes("ADMIN_STYLERADAR", null).getBody();

        assertThat(pendientes.tipoPublicador()).isEqualTo("TIENDA");
        assertThat(pendientes.total()).isEqualTo(1);
        assertThat(pendientes.grupos().get(0).almacen()).isEqualTo("Calle 85 Store");
        assertThat(pendientes.grupos().get(0).pendientes()).isEqualTo(1);
        assertThat(pendientes.grupos().get(0).prendas()).extracting(PrendaResponse::storeNit).containsExactly(NIT);
        assertThat(controller.moderar(prenda.getId(), null, "COMPRADOR,ADMIN_STYLERADAR", null, aprobar).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(controller.moderarGrupo(almacenId, "ADMIN", null, null, aprobar).getBody()).hasSize(1);
        assertThatThrownBy(() -> controller.moderar(prenda.getId(), "ADMIN_ALMACEN", null, null, aprobar))
                .isInstanceOf(AccesoDenegadoException.class);
        assertThatThrownBy(() -> controller.pendientes(null, null)).isInstanceOf(AccesoDenegadoException.class);
    }

    @Test
    void sinPendientesDevuelveTotalCero() {
        ModerarPublicacionUseCase useCase = mock(ModerarPublicacionUseCase.class);
        when(useCase.pendientesPorGrupo()).thenReturn(List.of());

        PendientesResponse pendientes = new ModeracionController(useCase, nits).pendientes(null, "ADMIN_STYLERADAR").getBody();

        assertThat(pendientes.total()).isZero();
        assertThat(pendientes.grupos()).isEmpty();
    }

    @Test
    void sinNitRegistradoElStoreNitVaVacio() {
        GestionarPrendaUseCase useCase = mock(GestionarPrendaUseCase.class);
        when(useCase.obtener(prenda.getId())).thenReturn(prenda);
        PrendaController controller = new PrendaController(useCase, mock(SincronizarAlmacenesUseCase.class), ids -> Map.of());

        assertThat(controller.obtener(prenda.getId()).getBody().storeNit()).isNull();
    }

    @Test
    void consultaLaDisponibilidadDeUnaPrenda() {
        ConsultarDisponibilidadUseCase useCase = mock(ConsultarDisponibilidadUseCase.class);
        when(useCase.consultar(prenda.getId())).thenReturn(new Disponibilidad(5, List.of("M")));

        DisponibilidadResponse respuesta = new DisponibilidadController(useCase).disponibilidad(prenda.getId()).getBody();

        assertThat(respuesta).isEqualTo(new DisponibilidadResponse(prenda.getId().toString(), 5, List.of("M")));
    }

    @Test
    void devuelveLasTallasYMedidasParaElProbador() {
        ConsultarDisponibilidadUseCase useCase = mock(ConsultarDisponibilidadUseCase.class);
        GuiaMedidas.MedidasTalla m = new GuiaMedidas.MedidasTalla("M", 92.0, 100.0, 74.0, 82.0, 98.0, 104.0);
        when(useCase.tallasYMedidas(prenda.getId()))
                .thenReturn(new ConsultarDisponibilidadUseCase.TallasYMedidas(List.of("M"), List.of(m)));
        UUID otra = UUID.randomUUID();
        when(useCase.tallasYMedidas(otra)).thenReturn(new ConsultarDisponibilidadUseCase.TallasYMedidas(null, List.of()));
        DisponibilidadController controller = new DisponibilidadController(useCase);

        assertThat(controller.tallasMedidas(prenda.getId()).getBody()).isEqualTo(new TallasMedidasResponse(List.of("M"),
                List.of(new TallasMedidasResponse.RangoMedidas("M", 92.0, 100.0, 74.0, 82.0, 98.0, 104.0))));
        assertThat(controller.tallasMedidas(otra).getBody().availableSizes()).isNull();
    }

    @Test
    void aceptaLosRolesNuevosDeMsAuth() {
        ModerarPublicacionUseCase useCase = mock(ModerarPublicacionUseCase.class);
        when(useCase.pendientesPorGrupo()).thenReturn(List.of());
        ModeracionController controller = new ModeracionController(useCase, nits);

        assertThat(controller.pendientes(null, "BUYER,STYLERADAR_ADMIN").getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThatThrownBy(() -> controller.pendientes("STORE_ADMIN", "BUYER")).isInstanceOf(AccesoDenegadoException.class);
    }

    @Test
    void elModeradorSaleDelUsuarioAutenticado() {
        ModerarPublicacionUseCase useCase = mock(ModerarPublicacionUseCase.class);
        UUID desdeToken = UUID.nameUUIDFromBytes("ms-auth:usuario:4".getBytes(java.nio.charset.StandardCharsets.UTF_8));
        when(useCase.moderar(prenda.getId(), EstadoPublicacion.APROBADA, null, desdeToken)).thenReturn(prenda);
        ModeracionController controller = new ModeracionController(useCase, nits);

        assertThat(controller.moderar(prenda.getId(), "STYLERADAR_ADMIN", null, "4",
                new ModeracionRequest(EstadoPublicacion.APROBADA, null, "admin-1")).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThatThrownBy(() -> controller.moderar(prenda.getId(), "STYLERADAR_ADMIN", null, null,
                new ModeracionRequest(EstadoPublicacion.APROBADA, null, "admin-1"))).isInstanceOf(ParametroInvalidoException.class);
    }
}
