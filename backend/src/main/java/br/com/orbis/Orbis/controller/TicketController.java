package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.model.TicketType;
import br.com.orbis.Orbis.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/events/{eventId}/tickets")
public class TicketController {

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    private static final Logger log = LoggerFactory.getLogger(TicketController.class);

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<String> sellTicket(
            @PathVariable Long eventId,
            @RequestParam Long userId,
            @RequestParam TicketType type) {
        log.info("[POST] /events/{}/tickets - Vendendo ingresso. eventId={}, userId={}, type={}",eventId, eventId, userId, type);
        try {
            ticketService.processTicketSale(eventId, userId, type);
            log.info("Ingresso vendido com sucesso para o evento {} e usuário {}.", eventId, userId);
            return ResponseEntity.ok("Ingresso vendido com sucesso!");
        } catch (IllegalStateException e) {
            log.warn("Falha ao vender ingresso: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Falha: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro inesperado ao vender ingresso", e);
            return ResponseEntity.internalServerError().body("Erro inesperado: " + e.getMessage());
        }
    }
}
