package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.messaging;

import edu.dosw.styleradar.itemcatalogo.domain.event.ModeracionCambiadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.event.PrendaPublicadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.event.PrendaRetiradaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RabbitMQPublisherAdapterTest {

    private static final String EXCHANGE = "styleradar.exchange";

    private final RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
    private final RabbitMQPublisherAdapter adapter = new RabbitMQPublisherAdapter(rabbitTemplate, EXCHANGE,
            "styleradar.catalogo.prenda.publicada", "styleradar.catalogo.prenda.retirada",
            "styleradar.catalogo.moderacion.cambiada");

    private final UUID prendaId = UUID.randomUUID();
    private final UUID almacenId = UUID.randomUUID();

    @SuppressWarnings("unchecked")
    private Map<String, Object> enviado(String routingKey) {
        ArgumentCaptor<Object> payload = ArgumentCaptor.forClass(Object.class);
        verify(rabbitTemplate).convertAndSend(eq(EXCHANGE), eq(routingKey), payload.capture());
        return (Map<String, Object>) payload.getValue();
    }

    @Test
    void publicaLaPrendaPublicadaConLosDatosDeLaPrenda() {
        adapter.publicar(new PrendaPublicadaEvent(prendaId, almacenId, "Chaqueta denim", "Chaqueta", "Azul", "Levis",
                "Urbano", new BigDecimal("190000"), "https://foto/1.jpg", LocalDateTime.of(2026, 11, 21, 18, 0),
                List.of("M", "L"), 7));

        Map<String, Object> payload = enviado("styleradar.catalogo.prenda.publicada");
        assertThat(payload).containsEntry("itemId", prendaId.toString())
                .containsEntry("tiendaId", almacenId.toString())
                .containsEntry("nombrePrenda", "Chaqueta denim")
                .containsEntry("categoria", "Chaqueta")
                .containsEntry("imagenUrl", "https://foto/1.jpg")
                .containsEntry("category", "OUTERWEAR")
                .containsEntry("availableSizes", List.of("M", "L"))
                .containsEntry("tallasDisponibles", List.of("M", "L"))
                .containsEntry("stockTotal", 7)
                .containsEntry("fechaPublicacion", "2026-11-21T18:00:00Z");
    }

    @Test
    void sinTallasConocidasNoEnviaAvailableSizes() {
        adapter.publicar(new PrendaPublicadaEvent(prendaId, almacenId, "Vestido", "Vestido", "Negro", "Luna Store",
                "FORMAL", new BigDecimal("90000"), null, LocalDateTime.now(), null, null));

        assertThat(enviado("styleradar.catalogo.prenda.publicada")).doesNotContainKey("availableSizes")
                .containsEntry("category", "ONE_PIECE");
    }

    @Test
    void mapeaLosTiposALasCategoriasDelProbador() {
        assertThat(RabbitMQPublisherAdapter.categoriaProbador("Camiseta")).isEqualTo("TOP");
        assertThat(RabbitMQPublisherAdapter.categoriaProbador("Pantalón")).isEqualTo("BOTTOM");
        assertThat(RabbitMQPublisherAdapter.categoriaProbador("Saco")).isEqualTo("OUTERWEAR");
        assertThat(RabbitMQPublisherAdapter.categoriaProbador("Calzado")).isEqualTo("FOOTWEAR");
        assertThat(RabbitMQPublisherAdapter.categoriaProbador("Accesorio")).isEqualTo("ACCESSORIES");
        assertThat(RabbitMQPublisherAdapter.categoriaProbador("Otro")).isNull();
        assertThat(RabbitMQPublisherAdapter.categoriaProbador(null)).isNull();
        assertThat(RabbitMQPublisherAdapter.utc(null)).isNull();
    }

    @Test
    void publicaLaPrendaRetirada() {
        adapter.publicar(new PrendaRetiradaEvent(prendaId, almacenId, "Retirada por el almacén", LocalDateTime.now()));

        assertThat(enviado("styleradar.catalogo.prenda.retirada")).containsEntry("prendaId", prendaId.toString())
                .containsEntry("motivo", "Retirada por el almacén");
    }

    @Test
    void publicaElCambioDeModeracion() {
        adapter.publicar(new ModeracionCambiadaEvent(prendaId, almacenId, "Chaqueta denim", EstadoPublicacion.RECHAZADA,
                "Foto borrosa", LocalDateTime.now(), 42L));

        assertThat(enviado("styleradar.catalogo.moderacion.cambiada")).containsEntry("propietarioUsuarioId", 42L)
                .containsEntry("estadoModeracion", "RECHAZADA")
                .containsEntry("observacion", "Foto borrosa");
    }

    @Test
    void siElBrokerFallaNoRompeLaOperacion() {
        doThrow(new AmqpException("sin conexión")).when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        assertThatCode(() -> adapter.publicar(new PrendaRetiradaEvent(prendaId, almacenId, "motivo", LocalDateTime.now())))
                .doesNotThrowAnyException();
    }
}
