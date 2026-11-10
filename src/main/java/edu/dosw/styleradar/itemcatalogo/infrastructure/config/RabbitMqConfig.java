package edu.dosw.styleradar.itemcatalogo.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Exchange y routing keys acordados con ms-inventario, ms-playlist y ms-pagos-suscripciones
@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE = "styleradar.events";

    public static final String RK_PRENDA_CREADA = "catalogo.prenda.creada";
    public static final String RK_PRENDA_ELIMINADA = "catalogo.prenda.eliminada";
    public static final String RK_STOCK_AGOTADO = "inventario.stock.agotado";
    public static final String RK_SUSCRIPCION_SUSPENDIDA = "tienda.suscripcion.suspendida";

    public static final String QUEUE_STOCK_AGOTADO = "q.item-catalogo.stock-agotado";
    public static final String QUEUE_SUSCRIPCION = "q.item-catalogo.suscripcion";

    @Bean
    public TopicExchange styleradarExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue stockAgotadoQueue() {
        return QueueBuilder.durable(QUEUE_STOCK_AGOTADO).build();
    }

    @Bean
    public Queue suscripcionQueue() {
        return QueueBuilder.durable(QUEUE_SUSCRIPCION).build();
    }

    @Bean
    public Binding stockAgotadoBinding(Queue stockAgotadoQueue, TopicExchange styleradarExchange) {
        return BindingBuilder.bind(stockAgotadoQueue).to(styleradarExchange).with(RK_STOCK_AGOTADO);
    }

    @Bean
    public Binding suscripcionBinding(Queue suscripcionQueue, TopicExchange styleradarExchange) {
        return BindingBuilder.bind(suscripcionQueue).to(styleradarExchange).with(RK_SUSCRIPCION_SUSPENDIDA);
    }

    // Mensajes en JSON, igual que los demás servicios
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
