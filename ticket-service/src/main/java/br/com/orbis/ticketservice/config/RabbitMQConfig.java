package br.com.orbis.ticketservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EVENT_CREATED_QUEUE = "event.created";
    public static final String EVENT_UPDATED_QUEUE = "event.updated";
    public static final String EVENT_DELETED_QUEUE = "event.deleted";

    @Bean
    public Queue eventCreatedQueue() {
        return new Queue(EVENT_CREATED_QUEUE, true);
    }

    @Bean
    public Queue eventUpdatedQueue() {
        return new Queue(EVENT_UPDATED_QUEUE, true);
    }

    @Bean
    public Queue eventDeletedQueue() {
        return new Queue(EVENT_DELETED_QUEUE, true);
    }
}
