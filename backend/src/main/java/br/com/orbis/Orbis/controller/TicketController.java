package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.model.TicketType;
import br.com.orbis.Orbis.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events/{eventId}/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<String> sellTicket(
            @PathVariable Long eventId,
            @RequestParam Long userId,
            @RequestParam TicketType type) {
        try {
            ticketService.processTicketSale(eventId, userId, type);
            return ResponseEntity.ok("Ingresso vendido com sucesso!");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Falha: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro inesperado: " + e.getMessage());
        }
    }
}
