package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.model.Ticket;
import br.com.orbis.Orbis.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping("/event/{eventId}/type/{ticketTypeId}")
    public ResponseEntity<Ticket> createTicket(
            @PathVariable Long eventId,
            @PathVariable Long ticketTypeId,
            @Valid @RequestBody Ticket ticket) {
        try {
            Ticket createdTicket = service.createTicket(eventId, ticketTypeId, ticket);
            return ResponseEntity.ok(createdTicket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> listTickets() {
        List<Ticket> tickets = service.listTickets();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Ticket>> listTicketsByEvent(@PathVariable Long eventId) {
        List<Ticket> tickets = service.listTicketsByEvent(eventId);
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<Ticket> updateTicket(
            @PathVariable Long ticketId,
            @Valid @RequestBody Ticket ticket) {
        try {
            Ticket updatedTicket = service.updateTicket(ticketId, ticket);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
        try {
            service.deleteTicket(ticketId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
