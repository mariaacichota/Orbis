package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.model.TicketType;
import br.com.orbis.Orbis.service.TicketTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket-types")
public class TicketTypeController {

    private final TicketTypeService service;

    public TicketTypeController(TicketTypeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TicketType> createTicketType(@Valid @RequestBody TicketType ticketType) {
        TicketType createdTicketType = service.createTicketType(ticketType);
        return ResponseEntity.ok(createdTicketType);
    }

    @GetMapping
    public ResponseEntity<List<TicketType>> listTicketTypes() {
        List<TicketType> ticketTypes = service.listTicketTypes();
        return ResponseEntity.ok(ticketTypes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketType> updateTicketType(
            @PathVariable Long id,
            @Valid @RequestBody TicketType ticketType) {
        try {
            TicketType updatedTicketType = service.updateTicketType(id, ticketType);
            return ResponseEntity.ok(updatedTicketType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketType(@PathVariable Long id) {
        try {
            service.deleteTicketType(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
