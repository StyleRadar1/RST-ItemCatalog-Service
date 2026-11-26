package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.domain.model.AtributosBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Fotografia;
import edu.dosw.styleradar.itemcatalogo.domain.model.IdAlmacen;
import edu.dosw.styleradar.itemcatalogo.domain.model.Precio;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarNitAlmacenUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.GestionarPrendaUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.SincronizarAlmacenesUseCase;
import edu.dosw.styleradar.itemcatalogo.infrastructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrendaController.class)
@Import({GlobalExceptionHandler.class, CorrelationIdFilter.class})
class PrendaControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GestionarPrendaUseCase gestionarPrenda;

    @MockBean
    private SincronizarAlmacenesUseCase almacenes;

    @MockBean
    private ConsultarNitAlmacenUseCase nitAlmacenes;

    private final UUID storeId = UUID.fromString("a2222222-2222-2222-2222-222222222222");

    private String payload(UUID bodyStoreId, String marca, String photoUrl) {
        return """
                {"almacenId":%s,"nombre":"Chaqueta QA","tipo":"Chaqueta","color":"Azul","marca":"%s",
                 "estilo":"Urbano","precio":190000,"fotografias":["%s"]}
                """.formatted(bodyStoreId == null ? "null" : "\"" + bodyStoreId + "\"", marca, photoUrl);
    }

    @Test
    void storeAdminPublicaConMarcaRealYUrlConEspacios() throws Exception {
        Prenda prenda = Prenda.publicar(storeId, "Chaqueta QA",
                new AtributosBusqueda("Chaqueta", "Azul", "Almacén Pruebas QA", "Urbano"),
                new Precio(new BigDecimal("190000")),
                List.of(new Fotografia("https://cdn.example.com/mi%20foto.jpg", 1)));
        when(almacenes.almacenDelUsuario(any())).thenReturn(Optional.empty());
        when(nitAlmacenes.nitsDe(any())).thenReturn(Map.of(storeId, "900555777-1"));
        when(gestionarPrenda.publicar(eq(storeId), eq("Chaqueta QA"), any(), any(),
                eq(List.of(new Fotografia("https://cdn.example.com/mi%20foto.jpg", 1)))))
                .thenReturn(prenda);

        mockMvc.perform(post("/garments")
                        .header("X-User-Role", "STORE_ADMIN")
                        .header("X-Store-Id", storeId.toString())
                        .header("X-Request-Id", "req-item-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload(storeId, "Almacén Pruebas QA", "https://cdn.example.com/mi foto.jpg")))
                .andExpect(status().isCreated())
                .andExpect(header().string("X-Correlation-Id", "req-item-123"))
                .andExpect(jsonPath("$.marca").value("Almacén Pruebas QA"))
                .andExpect(jsonPath("$.fotografias[0]").value("https://cdn.example.com/mi%20foto.jpg"));
    }

    @Test
    void storeAdminPublicaConElNitDeSuTiendaEnLaCabeceraYEnElCuerpo() throws Exception {
        UUID desdeNit = IdAlmacen.desdeNit("900555777-1");
        Prenda prenda = Prenda.publicar(desdeNit, "Chaqueta QA",
                new AtributosBusqueda("Chaqueta", "Azul", "Almacén Pruebas QA", "Urbano"),
                new Precio(new BigDecimal("190000")), List.of(new Fotografia("https://cdn.example.com/foto.jpg", 1)));
        when(almacenes.almacenDelUsuario(any())).thenReturn(Optional.empty());
        when(nitAlmacenes.nitsDe(any())).thenReturn(Map.of(desdeNit, "900555777-1"));
        when(gestionarPrenda.publicar(eq(desdeNit), eq("Chaqueta QA"), any(), any(), any())).thenReturn(prenda);

        mockMvc.perform(post("/garments")
                        .header("X-User-Role", "STORE_ADMIN")
                        .header("X-Store-Id", "900555777-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload(null, "Almacén Pruebas QA", "https://cdn.example.com/foto.jpg")
                                .replace("\"almacenId\":null", "\"almacenId\":\"900555777-1\"")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.storeNit").value("900555777-1"));
    }

    @Test
    void rechazaBuyerYNoAutenticadoYAlmacenAjenoCon403() throws Exception {
        UUID otroAlmacen = UUID.randomUUID();
        when(almacenes.almacenDelUsuario(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/garments")
                        .header("X-User-Role", "BUYER")
                        .header("X-Store-Id", storeId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload(storeId, "Almacén Pruebas QA", "https://cdn.example.com/1.jpg")))
                .andExpect(status().isForbidden())
                .andExpect(header().exists("X-Correlation-Id"))
                .andExpect(jsonPath("$.code").value("ACCESO_DENEGADO"));

        mockMvc.perform(post("/garments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload(storeId, "Almacén Pruebas QA", "https://cdn.example.com/1.jpg")))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/garments")
                        .header("X-User-Role", "STORE_ADMIN")
                        .header("X-Store-Id", storeId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload(otroAlmacen, "Almacén Pruebas QA", "https://cdn.example.com/1.jpg")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("ACCESO_DENEGADO"));

        mockMvc.perform(put("/garments/" + UUID.randomUUID())
                        .header("X-User-Role", "BUYER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload(storeId, "Almacén Pruebas QA", "https://cdn.example.com/1.jpg")))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/garments/" + UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

    @Test
    void rechazaUrlInvalidaCon400YMetodoNoSoportadoCon405() throws Exception {
        when(almacenes.almacenDelUsuario(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/garments")
                        .header("X-User-Role", "STORE_ADMIN")
                        .header("X-Store-Id", storeId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload(storeId, "Tienda Demo", "ftp://cdn.example.com/1.jpg")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("URL_FOTOGRAFIA_INVALIDA"));

        mockMvc.perform(patch("/garments/" + UUID.randomUUID()))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code").value("METODO_NO_PERMITIDO"));
    }
}
