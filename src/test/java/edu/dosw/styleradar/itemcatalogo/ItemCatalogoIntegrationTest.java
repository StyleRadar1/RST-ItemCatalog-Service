package edu.dosw.styleradar.itemcatalogo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Levanta el servicio completo contra un PostgreSQL real con las migraciones de Flyway
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class ItemCatalogoIntegrationTest {

    private static final String CALLE_85 = "a2222222-2222-2222-2222-222222222222";

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15.8-alpine");

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String prendaJson(String nombre) {
        return """
                {"almacenId":"%s","nombre":"%s","tipo":"Gorra","color":"Rojo","marca":"Calle 85",
                 "estilo":"Urbano","precio":45000,"fotografias":["https://foto/gorra.jpg"]}
                """.formatted(CALLE_85, nombre);
    }

    @Test
    void publicaModeraYBuscaUnaPrenda() throws Exception {
        String body = mockMvc.perform(post("/prendas").contentType(MediaType.APPLICATION_JSON).content(prendaJson("Gorra roja")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andReturn().getResponse().getContentAsString();
        JsonNode prenda = objectMapper.readTree(body);
        String id = prenda.get("id").asText();

        mockMvc.perform(get("/prendas").param("texto", "gorra roja"))
                .andExpect(jsonPath("$[*].id", not(hasItem(id))));

        mockMvc.perform(put("/prendas/" + id + "/moderacion").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"decision\":\"APROBADA\",\"moderadorId\":\"99999999-9999-9999-9999-999999999999\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADA"));

        mockMvc.perform(get("/prendas").param("texto", "gorra roja"))
                .andExpect(jsonPath("$[0].id").value(id));

        mockMvc.perform(delete("/prendas/" + id))
                .andExpect(jsonPath("$.estado").value("RETIRADA"));
    }

    @Test
    void editaUnaPrendaYVuelveAModeracion() throws Exception {
        String body = mockMvc.perform(post("/prendas").contentType(MediaType.APPLICATION_JSON).content(prendaJson("Gorra azul")))
                .andReturn().getResponse().getContentAsString();
        String id = objectMapper.readTree(body).get("id").asText();

        mockMvc.perform(put("/prendas/" + id).contentType(MediaType.APPLICATION_JSON).content(prendaJson("Gorra azul oscuro")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Gorra azul oscuro"))
                .andExpect(jsonPath("$.fotografias", hasSize(1)));
    }

    @Test
    void filtraYOrdenaLaBusqueda() throws Exception {
        mockMvc.perform(get("/prendas").param("tipo", "Chaqueta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Chaqueta denim oversize"));

        mockMvc.perform(get("/prendas").param("orden", "REPUTACION"))
                .andExpect(jsonPath("$[0].nombre").value("Blazer de lino"));
    }

    @Test
    void consultaElCatalogoDeUnAlmacen() throws Exception {
        mockMvc.perform(get("/almacenes/" + CALLE_85 + "/catalogo/resumen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.almacenId").value(CALLE_85));

        mockMvc.perform(get("/almacenes/" + CALLE_85 + "/novedades"))
                .andExpect(jsonPath("$[*].nombre", not(hasItem("Hoodie gráfico"))));
    }

    @Test
    void listaLasPendientesPorAlmacen() throws Exception {
        mockMvc.perform(get("/prendas/pendientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['a3333333-3333-3333-3333-333333333333']", hasSize(1)));
    }

    @Test
    void respondeErroresClaros() throws Exception {
        mockMvc.perform(post("/prendas").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"\"}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/prendas/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());

        mockMvc.perform(put("/prendas/b1111111-1111-1111-1111-111111111111/moderacion").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"decision\":\"APROBADA\",\"moderadorId\":\"99999999-9999-9999-9999-999999999999\"}"))
                .andExpect(status().isUnprocessableEntity());
    }
}
