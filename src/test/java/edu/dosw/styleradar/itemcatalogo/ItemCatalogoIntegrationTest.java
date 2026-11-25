package edu.dosw.styleradar.itemcatalogo;

import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.StockPrendaPort;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;
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
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class ItemCatalogoIntegrationTest {

    private static final String EXCHANGE = "styleradar.exchange";
    private static final String CALLE_85 ="a2222222-2222-2222-2222-222222222222";
    private static final String HOODIE = "b3333333-3333-3333-3333-333333333333";
    private static final String ALMACEN = "X-Almacen-Id";
    private static final String ROL = "X-User-Role";
    private static final String ADMIN = "ADMIN_STYLERADAR";

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

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
        registry.add("tiendas.carga.habilitada", () -> "false");
    }

    // ms-inventario no existe en el CI; se simula que la prenda quedó sin unidades
    @MockBean
    private StockPrendaPort stock;

    @BeforeEach
    void inventarioSinUnidades() {
        when(stock.disponibilidad(any())).thenReturn(Optional.of(Disponibilidad.sinStock()));
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
                {"almacenId":"%s","nombre":"%s","tipo":"Accesorio","color":"Negro","marca":"Calle 85 Store",
                 "estilo":"Urbano","precio":45000,"fotografias":["https://foto/gorra.jpg"]}
                """.formatted(CALLE_85, nombre);
    }

    private String publicar(String nombre) throws Exception {
        String body = mockMvc.perform(post("/garments").contentType(MediaType.APPLICATION_JSON).content(prendaJson(nombre)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(body).get("id").asText();
    }

    @Test
    void publicaModeraYBuscaUnaPrenda() throws Exception {
        String id = publicar("Gorra roja");

        mockMvc.perform(get("/garments").param("texto", "gorra roja"))
                .andExpect(jsonPath("$[*].id", not(hasItem(id))));

        mockMvc.perform(put("/garments/" + id + "/moderation").header(ROL, ADMIN).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"decision\":\"APROBADA\",\"moderadorId\":\"99999999-9999-9999-9999-999999999999\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADA"));

        mockMvc.perform(get("/garments").param("texto", "gorra roja"))
                .andExpect(jsonPath("$[0].id").value(id));
    }

    @Test
    void soloElDuenoEditaYRetiraLaPrenda() throws Exception {
        String id = publicar("Gorra azul");

        mockMvc.perform(put("/garments/" + id).header(ALMACEN, "a1111111-1111-1111-1111-111111111111")
                        .contentType(MediaType.APPLICATION_JSON).content(prendaJson("Gorra ajena")))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/garments/" + id).header(ALMACEN, CALLE_85)
                        .contentType(MediaType.APPLICATION_JSON).content(prendaJson("Gorra azul oscuro")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Gorra azul oscuro"))
                .andExpect(jsonPath("$.fotografias", hasSize(1)));

        mockMvc.perform(delete("/garments/" + id))
                .andExpect(status().isForbidden());
    }

    private String escuchar(String cola, String routingKey) {
        TopicExchange exchange = new TopicExchange(EXCHANGE, true, false);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareQueue(new Queue(cola, false, false, true));
        amqpAdmin.declareBinding(BindingBuilder.bind(new Queue(cola)).to(exchange).with(routingKey));
        return cola;
    }

    @Test
    void alRetirarSePublicaLaPrendaRetirada() throws Exception {
        // Arrange
        String cola = escuchar("q.test.prenda-retirada", "styleradar.catalogo.prenda.retirada");
        String id = publicar("Gorra verde");

        // Act
        mockMvc.perform(delete("/garments/" + id).header(ALMACEN, CALLE_85))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("RETIRADA"));

        // Assert
        Object mensaje = rabbitTemplate.receiveAndConvert(cola, 5000);
        assertThat(mensaje).isInstanceOf(Map.class);
        assertThat(((Map<?, ?>) mensaje).get("prendaId")).isEqualTo(id);
    }

    @Test
    void alAprobarSePublicaLaPrendaPublicada() throws Exception {
        String cola = escuchar("q.test.prenda-publicada", "styleradar.catalogo.prenda.publicada");
        String id = publicar("Gorra amarilla");

        mockMvc.perform(put("/garments/" + id + "/moderation").header(ROL, ADMIN).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"decision\":\"APROBADA\",\"moderadorId\":\"99999999-9999-9999-9999-999999999999\"}"))
                .andExpect(status().isOk());

        Object mensaje = rabbitTemplate.receiveAndConvert(cola, 5000);
        assertThat(((Map<?, ?>) mensaje).get("itemId")).isEqualTo(id);
        assertThat(((Map<?, ?>) mensaje).get("nombrePrenda")).isEqualTo("Gorra amarilla");
        assertThat(((Map<?, ?>) mensaje).get("imagenUrl")).isEqualTo("https://foto/gorra.jpg");
    }

    @Test
    void tambienRecibeElEventoDeStockAgotado() throws Exception {
        rabbitTemplate.convertAndSend(EXCHANGE, "styleradar.inventario.stock.agotado",
                Map.of("data", Map.of("prendaId", "b4444444-4444-4444-4444-444444444444", "estado", "AGOTADA")));

        boolean agotada = false;
        for (int i = 0; i < 50 && !agotada; i++) {
            Thread.sleep(100);
            String body = mockMvc.perform(get("/garments/b4444444-4444-4444-4444-444444444444")).andReturn().getResponse().getContentAsString();
            agotada = !objectMapper.readTree(body).get("disponible").asBoolean();
        }

        assertThat(agotada).isTrue();
    }

    @Test
    void unMensajeInvalidoTerminaEnLaColaDeErrores() {
        rabbitTemplate.convertAndSend(EXCHANGE, "styleradar.inventario.actualizado", Map.of("itemId", "no-es-uuid"));

        Object mensaje = rabbitTemplate.receiveAndConvert("styleradar.catalogo.dlq", 5000);
        assertThat(mensaje).isInstanceOf(Map.class);
    }

    @Test
    void alAgotarseEnInventarioSeOcultaDeLaBusqueda() throws Exception {
        rabbitTemplate.convertAndSend(EXCHANGE, "styleradar.inventario.actualizado",
                Map.of("itemId", HOODIE, "talla", "M", "stockActual", 0));

        boolean agotada = false;
        for (int i = 0; i < 50 && !agotada; i++) {
            Thread.sleep(100);
            String body = mockMvc.perform(get("/garments/" + HOODIE)).andReturn().getResponse().getContentAsString();
            agotada = !objectMapper.readTree(body).get("disponible").asBoolean();
        }

        assertThat(agotada).isTrue();
        mockMvc.perform(get("/garments").param("texto", "hoodie"))
                .andExpect(jsonPath("$[*].id", not(hasItem(HOODIE))));
    }

    @Test
    void filtraYOrdenaLaBusqueda() throws Exception {
        mockMvc.perform(get("/garments").param("tipo", "Chaqueta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Chaqueta denim oversize"));

        mockMvc.perform(get("/garments").param("orden", "REPUTACION"))
                .andExpect(jsonPath("$[0].nombre").value("Blazer de lino"));
    }

    @Test
    void consultaElCatalogoDeUnAlmacen() throws Exception {
        mockMvc.perform(get("/stores/" + CALLE_85 + "/catalog/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.almacenId").value(CALLE_85));

        mockMvc.perform(get("/stores/" + CALLE_85 + "/new-arrivals"))
                .andExpect(jsonPath("$[*].nombre", not(hasItem("Hoodie gráfico"))));
    }

    @Test
    void listaLasPendientesPorAlmacen() throws Exception {
        mockMvc.perform(get("/garments/pending").header(ROL, ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grupos[?(@.almacenId == 'a3333333-3333-3333-3333-333333333333')].pendientes", hasItem(1)));
    }

    @Test
    void respondeErroresClaros() throws Exception {
        mockMvc.perform(post("/garments").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"\"}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/garments/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/garments").param("tipo", "Gorra").param("estilo", "Elegante").param("lat", "95").param("lng", "-74"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.tipo").exists())
                .andExpect(jsonPath("$.errors.estilo").exists())
                .andExpect(jsonPath("$.errors.lat").value("La latitud debe estar entre -90 y 90"));

        mockMvc.perform(get("/garments").param("precioMin", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.precioMin").value("El precio mínimo no puede ser negativo"));

        mockMvc.perform(get("/garments").param("precioMin", "200000").param("precioMax", "100000"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("El precio mínimo no puede ser mayor que el precio máximo"));

        mockMvc.perform(put("/garments/b1111111-1111-1111-1111-111111111111/moderation").header(ROL, ADMIN).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"decision\":\"APROBADA\",\"moderadorId\":\"99999999-9999-9999-9999-999999999999\"}"))
                .andExpect(status().isUnprocessableEntity());

        mockMvc.perform(get("/garments/pending").header(ROL, "COMPRADOR"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("ACCESO_DENEGADO"));
    }
}
