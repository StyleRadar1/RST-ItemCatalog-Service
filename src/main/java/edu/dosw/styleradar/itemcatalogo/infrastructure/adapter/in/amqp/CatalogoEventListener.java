package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.amqp;

import edu.dosw.styleradar.itemcatalogo.domain.port.in.ActualizarDisponibilidadUseCase;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

// Los mensajes que no se pueden procesar se rechazan y RabbitMQ los manda a la cola de errores
@Component
public class CatalogoEventListener {

    private final ActualizarDisponibilidadUseCase actualizarDisponibilidad;

    public CatalogoEventListener(ActualizarDisponibilidadUseCase actualizarDisponibilidad) {
        this.actualizarDisponibilidad = actualizarDisponibilidad;
    }

    // Solo interesa cuando la prenda se queda sin unidades
    @RabbitListener(queues = "${styleradar.rabbitmq.queues.inventario}")
    public void onInventarioActualizado(Map<String, Object> payload) {
        UUID prendaId = leerUuid(payload, "prendaId", "itemId");
        if (prendaId == null) {
            throw new AmqpRejectAndDontRequeueException("Evento de inventario sin prendaId válido: " + payload);
        }
        if (estaAgotada(payload)) {
            actualizarDisponibilidad.marcarAgotada(prendaId);
        }
    }

    @RabbitListener(queues = "${styleradar.rabbitmq.queues.suscripcion}")
    public void onSuscripcionSuspendida(Map<String, Object> payload) {
        UUID almacenId = leerUuid(payload, "almacenId", "tiendaId", "sedeId");
        if (almacenId == null) {
            throw new AmqpRejectAndDontRequeueException("Evento de suscripción sin almacenId válido: " + payload);
        }
        actualizarDisponibilidad.suspenderAlmacen(almacenId);
    }

    static boolean estaAgotada(Map<String, Object> payload) {
        Object estado = leer(payload, "estado");
        Object stock = leer(payload, "stockActual");
        return "AGOTADA".equals(String.valueOf(estado)) || (stock != null && "0".equals(stock.toString()));
    }

    // El dato puede venir en la raíz o dentro de "data", según el servicio que lo envía
    static UUID leerUuid(Map<String, Object> payload, String... claves) {
        for (String clave : claves) {
            Object valor = leer(payload, clave);
            if (valor != null) {
                try {
                    return UUID.fromString(valor.toString());
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
        }
        return null;
    }

    private static Object leer(Map<String, Object> payload, String clave) {
        if (payload == null) {
            return null;
        }
        Object valor = payload.get(clave);
        if (valor == null && payload.get("data") instanceof Map<?, ?> data) {
            valor = data.get(clave);
        }
        return valor;
    }
}
