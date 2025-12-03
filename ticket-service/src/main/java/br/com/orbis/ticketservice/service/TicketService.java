package br.com.orbis.ticketservice.service;

import br.com.orbis.ticketservice.client.EventClient;
import br.com.orbis.ticketservice.model.Ticket;
import br.com.orbis.ticketservice.model.TicketType;
import br.com.orbis.ticketservice.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventClient eventClient;

    public TicketService(TicketRepository ticketRepository, EventClient eventClient) {
        this.ticketRepository = ticketRepository;
        this.eventClient = eventClient;
    }

    public Ticket createTicket(TicketRequest request) {

        Ticket ticket = new Ticket(
                request.getType(),
                request.getEventId(),
                request.getUserId(),
                request.getBasePrice()
        );

        return ticketRepository.save(ticket);
    }
    
    public boolean processTicketSale(Long eventId, Long userId, TicketType type) {
        EventClient.EventResponse event = eventClient.getEventById(eventId);

        if (event == null) {
            throw new IllegalStateException("Evento não encontrado");
        }

        long sold = ticketRepository.countByEventId(eventId);
        if (sold >= event.getMaxTickets()) {
            throw new IllegalStateException("Não há mais tickets disponíveis para o evento.");
        }

        Ticket ticket = new Ticket(type, eventId, userId, event.getBaseTicketPrice());
        ticketRepository.save(ticket);
        return true;
    }
}

