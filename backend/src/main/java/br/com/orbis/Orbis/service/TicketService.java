package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.model.Ticket;
import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.model.TicketType;
import br.com.orbis.Orbis.repository.TicketRepository;
import br.com.orbis.Orbis.repository.EventRepository;
import br.com.orbis.Orbis.repository.TicketTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;

    public TicketService(TicketRepository ticketRepository, EventRepository eventRepository, TicketTypeRepository ticketTypeRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.ticketTypeRepository = ticketTypeRepository;
    }

    public Ticket createTicket(Long eventId, Long ticketTypeId, Ticket ticket) {
        Optional<Event> event = eventRepository.findById(eventId);
        Optional<TicketType> ticketType = ticketTypeRepository.findById(ticketTypeId);

        if (event.isEmpty()) {
            throw new IllegalArgumentException("Event not found");
        }
        if (ticketType.isEmpty()) {
            throw new IllegalArgumentException("Ticket type not found");
        }

        ticket.setEvent(event.get());
        ticket.setTicketType(ticketType.get());
        return ticketRepository.save(ticket);
    }

    public List<Ticket> listTickets() {
        return ticketRepository.findAll();
    }

    public List<Ticket> listTicketsByEvent(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    public Ticket updateTicket(Long ticketId, Ticket updatedTicket) {
        Optional<Ticket> existingTicket = ticketRepository.findById(ticketId);
        if (existingTicket.isEmpty()) {
            throw new IllegalArgumentException("Ticket not found");
        }

        Ticket ticketToUpdate = existingTicket.get();
        ticketToUpdate.setPrice(updatedTicket.getPrice());
        ticketToUpdate.setAvailableQuantity(updatedTicket.getAvailableQuantity());
        return ticketRepository.save(ticketToUpdate);
    }

    public void deleteTicket(Long ticketId) {
        Optional<Ticket> existingTicket = ticketRepository.findById(ticketId);
        if (existingTicket.isEmpty()) {
            throw new IllegalArgumentException("Ticket not found");
        }
        ticketRepository.delete(existingTicket.get());
    }
}
