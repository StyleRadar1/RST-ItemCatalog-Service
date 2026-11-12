package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.messaging;

import edu.dosw.styleradar.itemcatalogo.domain.event.ModeracionCambiadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.event.PrendaPublicadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.event.PrendaRetiradaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.EventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

// Los nombres de los campos siguen los contratos de los servicios que consumen estos eventos
@Component
public class RabbitMQPublisherAdapter implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQPublisherAdapter.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String rkPublicada;
    private final String rkRetirada;
    private final String rkModeracion;

    public RabbitMQPublisherAdapter(RabbitTemplate rabbitTemplate,
                                    @Value("${styleradar.rabbitmq.exchange}") String exchange,
                                    @Value("${styleradar.rabbitmq.routing-keys.prenda-publicada}") String rkPublicada,
                                    @Value("${styleradar.rabbitmq.routing-keys.prenda-retirada}") String rkRetirada,
                                    @Value("${styleradar.rabbitmq.routing-keys.moderacion-cambiada}") String rkModeracion) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.rkPublicada = rkPublicada;
        this.rkRetirada = rkRetirada;
        this.rkModeracion = rkModeracion;
    }

    @Override
    public void publicar(PrendaPublicadaEvent evento) {
        Map<String, Object> payload = base(evento.prendaId(), evento.almacenId());
        payload.put("nombrePrenda", evento.nombre());
        payload.put("categoria", evento.tipo());
        payload.put("color", evento.color());
        payload.put("marca", evento.marca());
        payload.put("estilo", evento.estilo());
        payload.put("precio", evento.precio());
        payload.put("fechaPublicacion", evento.fecha().toString());
        enviar(rkPublicada, payload);
    }

    @Override
    public void publicar(PrendaRetiradaEvent evento) {
        Map<String, Object> payload = base(evento.prendaId(), evento.almacenId());
        payload.put("motivo", evento.motivo());
        payload.put("fechaEvento", evento.fecha().toString());
        enviar(rkRetirada, payload);
    }

    @Override
    public void publicar(ModeracionCambiadaEvent evento) {
        Map<String, Object> payload = base(evento.prendaId(), evento.almacenId());
        payload.put("nombrePrenda", evento.nombre());
        payload.put("estadoModeracion", evento.estado().name());
        payload.put("observacion", evento.motivo());
        payload.put("fechaModeracion", evento.fecha().toString());
        enviar(rkModeracion, payload);
    }

    // itemId y prendaId son el mismo id; tiendaId y almacenId también
    private Map<String, Object> base(UUID prendaId, UUID almacenId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("eventoId", UUID.randomUUID().toString());
        payload.put("productor", "ms-item-catalogo");
        payload.put("itemId", prendaId.toString());
        payload.put("prendaId", prendaId.toString());
        payload.put("tiendaId", almacenId.toString());
        payload.put("almacenId", almacenId.toString());
        return payload;
    }

    private void enviar(String routingKey, Map<String, Object> payload) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, payload);
        } catch (AmqpException e) {
            // Si el broker no responde, la operación ya quedó guardada; solo se registra
            log.warn("No se pudo publicar {} de la prenda {}: {}", routingKey, payload.get("prendaId"), e.getMessage());
        }
    }
}
