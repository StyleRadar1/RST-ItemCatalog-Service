package edu.dosw.styleradar.itemcatalogo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Levanta el servicio completo contra PostgreSQL y RabbitMQ reales
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class ItemCatalogoIntegrationTest {

    private static final String CALLE_85 = "a2222222-2222-2222-2222-222222222222";
    private static final String HOODIE = "b3333333-3333-3333-3333-333333333333";
    private static final String ALMACEN = "X-Almacen-Id";

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15.8-alpine");

    @Container
    static final RabbitMQContainer RABBIT = new RabbitMQContainer("rabbitmq:3.13.7-management-alpine");

    @DynamicPropertySource
    static void propiedades(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.flyway.locations", () -> "classpath:db/migration,classpath:db/seed");
        registry.add("spring.rabbitmq.host", RABBIT::getHost);
        registry.add("spring.rabbitmq.port", RABBIT::getAmqpPort);
        registry.add("spring.rabbitmq.username", RABBIT::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBIT::getAdminPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    private String prendaJson(String nombre) {
        return """
                {"almacenId":"%s","nombre":"%s","tipo":"Gorra","color":"Rojo","marca":"Calle 85",
                 "estilo":"Urbano","precio":45000,"fotografias":["https://foto/gorra.jpg"]}
                """.formatted(CALLE_85, nombre);
    }

    private String publicar(String nombre) throws Exception {
        String body = mockMvc.perform(post("/prendas").contentType(MediaType.APPLICATION_JSON).content(prendaJson(nombre)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(body).get("id").asText();
    }

    @Test
    void publicaModeraYBuscaUnaPrenda() throws Exception {
        String id = publicar("Gorra roja");

        mockMvc.perform(get("/prendas").param("texto", "gorra roja"))
                .andExpect(jsonPath("$[*].id", not(hasItem(id))));

        mockMvc.perform(put("/prendas/" + id + "/moderacion").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"decision\":\"APROBADA\",\"moderadorId\":\"99999999-9999-9999-9999-999999999999\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADA"));

        mockMvc.perform(get("/prendas").param("texto", "gorra roja"))
                .andExpect(jsonPath("$[0].id").value(id));
    }

    @Test
    void soloElDuenoEditaYRetiraLaPrenda() throws Exception {
        String id = publicar("Gorra azul");

        mockMvc.perform(put("/prendas/" + id).header(ALMACEN, "a1111111-1111-1111-1111-111111111111")
                        .contentType(MediaType.APPLICATION_JSON).content(prendaJson("Gorra ajena")))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/prendas/" + id).header(ALMACEN, CALLE_85)
                        .contentType(MediaType.APPLICATION_JSON).content(prendaJson("Gorra azul oscuro")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Gorra azul oscuro"))
                .andExpect(jsonPath("$.fotografias", hasSize(1)));

        mockMvc.perform(delete("/prendas/" + id))
                .andExpect(status().isForbidden());
    }

    @Test
    void alRetirarSePublicaElEventoParaPlaylist() throws Exception {
        // Arrange: una cola de prueba escucha la misma routing key que ms-playlist
        TopicExchange exchange = new TopicExchange("styleradar.events", true, false);
        Queue cola = new Queue("q.test.prenda-eliminada", false, false, true);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareQueue(cola);
        amqpAdmin.declareBinding(BindingBuilder.bind(cola).to(exchange).with("catalogo.prenda.eliminada"));
        String id = publicar("Gorra verde");

        // Act
        mockMvc.perform(delete("/prendas/" + id).header(ALMACEN, CALLE_85))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("RETIRADA"));

        // Assert
        Object mensaje = rabbitTemplate.receiveAndConvert("q.test.prenda-eliminada", 5000);
        assertThat(mensaje).isInstanceOf(Map.class);
        assertThat(((Map<?, ?>) mensaje).get("prendaId")).isEqualTo(id);
    }

    @Test
    void alAgotarseEnInventarioSeOcultaDeLaBusqueda() throws Exception {
        // Mismo formato que publica ms-inventario
        rabbitTemplate.convertAndSend("styleradar.events", "inventario.stock.agotado",
                Map.of("eventType", "inventario.stock.agotado", "data", Map.of("prendaId", HOODIE, "talla", "M")));

        boolean agotada = false;
        for (int i = 0; i < 50 && !agotada; i++) {
            Thread.sleep(100);
            String body = mockMvc.perform(get("/prendas/" + HOODIE)).andReturn().getResponse().getContentAsString();
            agotada = !objectMapper.readTree(body).get("disponible").asBoolean();
        }

        assertThat(agotada).isTrue();
        mockMvc.perform(get("/prendas").param("texto", "hoodie"))
                .andExpect(jsonPath("$[*].id", not(hasItem(HOODIE))));
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
                .andExpect(jsonPath("$.grupos[?(@.almacenId == 'a3333333-3333-3333-3333-333333333333')].pendientes", hasItem(1)));
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
