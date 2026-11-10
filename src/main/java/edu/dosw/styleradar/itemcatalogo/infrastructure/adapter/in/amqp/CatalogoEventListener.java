package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.amqp;

import edu.dosw.styleradar.itemcatalogo.domain.port.in.ActualizarDisponibilidadUseCase;
import edu.dosw.styleradar.itemcatalogo.infrastructure.config.RabbitMqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class CatalogoEventListener {

    private static final Logger log = LoggerFactory.getLogger(CatalogoEventListener.class);

    private final ActualizarDisponibilidadUseCase actualizarDisponibilidad;

    public CatalogoEventListener(ActualizarDisponibilidadUseCase actualizarDisponibilidad) {
        this.actualizarDisponibilidad = actualizarDisponibilidad;
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE_STOCK_AGOTADO)
    public void onStockAgotado(Map<String, Object> payload) {
        UUID prendaId = leerUuid(payload, "prendaId");
        if (prendaId == null) {
            log.warn("Evento de stock agotado sin prendaId válido: {}", payload);
            return;
        }
        actualizarDisponibilidad.marcarAgotada(prendaId);
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE_SUSCRIPCION)
    public void onSuscripcionSuspendida(Map<String, Object> payload) {
        UUID almacenId = leerUuid(payload, "almacenId");
        if (almacenId == null) {
            almacenId = leerUuid(payload, "sedeId");
        }
        if (almacenId == null) {
            log.warn("Evento de suscripción suspendida sin almacenId válido: {}", payload);
            return;
        }
        actualizarDisponibilidad.suspenderAlmacen(almacenId);
    }

    // El dato puede venir en la raíz o dentro de "data", según el servicio que lo envía
    static UUID leerUuid(Map<String, Object> payload, String clave) {
        if (payload == null) {
            return null;
        }
        Object valor = payload.get(clave);
        if (valor == null && payload.get("data") instanceof Map<?, ?> data) {
            valor = data.get(clave);
        }
        try {
            return valor == null ? null : UUID.fromString(valor.toString());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
