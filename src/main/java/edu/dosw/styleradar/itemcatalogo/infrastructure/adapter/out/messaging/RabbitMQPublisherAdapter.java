package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.messaging;

import edu.dosw.styleradar.itemcatalogo.domain.event.NuevaPrendaCreadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.event.PrendaEliminadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.EventPublisherPort;
import edu.dosw.styleradar.itemcatalogo.infrastructure.config.RabbitMqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class RabbitMQPublisherAdapter implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQPublisherAdapter.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicar(NuevaPrendaCreadaEvent evento) {
        enviar(RabbitMqConfig.RK_PRENDA_CREADA, evento.prendaId(), evento.almacenId(), evento.fecha());
    }

    @Override
    public void publicar(PrendaEliminadaEvent evento) {
        enviar(RabbitMqConfig.RK_PRENDA_ELIMINADA, evento.prendaId(), evento.almacenId(), evento.fecha());
    }

    // prendaId va en la raíz porque así lo lee ms-playlist
    private void enviar(String routingKey, UUID prendaId, UUID almacenId, LocalDateTime fecha) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("eventId", UUID.randomUUID().toString());
        payload.put("eventType", routingKey);
        payload.put("timestamp", fecha.toString());
        payload.put("producer", "ms-item-catalogo");
        payload.put("prendaId", prendaId.toString());
        payload.put("almacenId", almacenId.toString());
        try {
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, routingKey, payload);
        } catch (AmqpException e) {
            // Si el broker no responde, la operación ya quedó guardada; solo se registra
            log.warn("No se pudo publicar {} de la prenda {}: {}", routingKey, prendaId, e.getMessage());
        }
    }
}
