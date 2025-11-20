package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.model.Ticket;
import br.com.orbis.Orbis.model.TicketType;
import br.com.orbis.Orbis.repository.EventRepository;
import br.com.orbis.Orbis.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final EventRepository eventRepository;

    private final TicketRepository ticketRepository;

    public TicketService(EventRepository eventRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    public boolean processTicketSale(Long eventId, Long userId, TicketType type) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        long sold = ticketRepository.countByEventId(eventId);
        if (sold >= event.getMaxTickets()) {
            throw new IllegalStateException("Não há mais tickets disponíveis para o evento.");
        }

        Ticket ticket = new Ticket(type, event, userId);
        ticketRepository.save(ticket);
        return true;
    }
}
