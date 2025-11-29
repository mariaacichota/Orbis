package br.com.orbis.eventservice.messaging;

import br.com.orbis.eventservice.config.RabbitMQConfig;
import br.com.orbis.eventservice.model.Event;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEventCreated(Event event) {
        EventMessage message = convertToMessage(event);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EVENT_CREATED_QUEUE, message);
    }

    public void publishEventUpdated(Event event) {
        EventMessage message = convertToMessage(event);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EVENT_UPDATED_QUEUE, message);
    }

    public void publishEventDeleted(Long eventId) {
        EventDeletedMessage message = new EventDeletedMessage(eventId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EVENT_DELETED_QUEUE, message);
    }

    private EventMessage convertToMessage(Event event) {
        return new EventMessage(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getTime(),
                event.getLocation(),
                event.getMaxTickets(),
                event.getOrganizerId(),
                event.getBaseTicketPrice(),
                event.getImageUrl()
        );
    }
}

