package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.model.Ticket;
import br.com.orbis.Orbis.model.TicketType;
import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.repository.EventRepository;
import br.com.orbis.Orbis.repository.TicketRepository;
import br.com.orbis.Orbis.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final EventRepository eventRepository;

    private final TicketRepository ticketRepository;

    private final UserRepository userRepository;

    public TicketService(EventRepository eventRepository, TicketRepository ticketRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public boolean processTicketSale(Long eventId, Long userId, TicketType type) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        long sold = ticketRepository.countByEventId(eventId);
        if (sold >= event.getMaxTickets()) {
            throw new IllegalStateException("Não há mais tickets disponíveis para o evento.");
        }

        Ticket ticket = new Ticket(type, event, user);
        ticketRepository.save(ticket);
        return true;
    }
}
