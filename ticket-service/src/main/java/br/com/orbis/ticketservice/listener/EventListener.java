package br.com.orbis.ticketservice.listener;

import br.com.orbis.ticketservice.config.RabbitMQConfig;
import br.com.orbis.ticketservice.dto.EventMessageDTO;
import br.com.orbis.ticketservice.dto.EventDeletedMessageDTO;
import br.com.orbis.ticketservice.service.EventSyncService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    private final EventSyncService service;

    public EventListener(EventSyncService service) {
        this.service = service;
    }

    @RabbitListener(queues = RabbitMQConfig.EVENT_CREATED_QUEUE)
    public void onEventCreated(EventMessageDTO message) {
        service.saveEvent(message);
    }

    @RabbitListener(queues = RabbitMQConfig.EVENT_UPDATED_QUEUE)
    public void onEventUpdated(EventMessageDTO message) {
        service.updateEvent(message);
    }

    @RabbitListener(queues = RabbitMQConfig.EVENT_DELETED_QUEUE)
    public void onEventDeleted(EventDeletedMessageDTO message) {
        service.deleteEvent(message.getId());
    }
}
