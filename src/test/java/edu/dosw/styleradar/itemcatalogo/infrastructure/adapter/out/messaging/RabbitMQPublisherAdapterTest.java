package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.messaging;

import edu.dosw.styleradar.itemcatalogo.domain.event.NuevaPrendaCreadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.event.PrendaEliminadaEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
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

    private final RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
    private final RabbitMQPublisherAdapter adapter = new RabbitMQPublisherAdapter(rabbitTemplate);

    @Test
    @SuppressWarnings("unchecked")
    void publicaLaPrendaEliminadaConPrendaIdEnLaRaiz() {
        UUID prendaId = UUID.randomUUID();
        ArgumentCaptor<Object> payload = ArgumentCaptor.forClass(Object.class);

        adapter.publicar(new PrendaEliminadaEvent(prendaId, UUID.randomUUID(), LocalDateTime.now()));

        verify(rabbitTemplate).convertAndSend(eq("styleradar.events"), eq("catalogo.prenda.eliminada"), payload.capture());
        assertThat((Map<String, Object>) payload.getValue()).containsEntry("prendaId", prendaId.toString());
    }

    @Test
    void publicaLaPrendaCreada() {
        adapter.publicar(new NuevaPrendaCreadaEvent(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now()));

        verify(rabbitTemplate).convertAndSend(eq("styleradar.events"), eq("catalogo.prenda.creada"), any(Object.class));
    }

    @Test
    void siElBrokerFallaNoRompeLaOperacion() {
        doThrow(new AmqpException("sin conexión")).when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        assertThatCode(() -> adapter.publicar(new NuevaPrendaCreadaEvent(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now())))
                .doesNotThrowAnyException();
    }
}
