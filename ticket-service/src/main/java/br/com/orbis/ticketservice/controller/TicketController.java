package br.com.orbis.ticketservice.controller;

import br.com.orbis.ticketservice.dto.TicketRequest;
import br.com.orbis.ticketservice.dto.TicketResponse;
import br.com.orbis.ticketservice.service.TicketService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private static final Logger log = LoggerFactory.getLogger(TicketController.class);
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> sellTicket(@RequestBody @Valid TicketRequest request) {
        log.info("[POST] /tickets - Vendendo ingresso. eventId={}, userId={}, type={}",
                request.getEventId(), request.getUserId(), request.getType());
        try {
            TicketResponse response = ticketService.sellTicket(request);
            log.info("Ingresso vendido com sucesso para o evento {} e usuário {}.",
                    request.getEventId(), request.getUserId());
            return ResponseEntity.ok(response);

        } catch (IllegalStateException e) {
            log.warn("Falha ao vender ingresso: {}", e.getMessage());
            return ResponseEntity.badRequest().build(); // se quiser, dá pra devolver um body com erro
        } catch (Exception e) {
            log.error("Erro inesperado ao vender ingresso", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicket(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketResponse>> getTicketsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUser(userId));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TicketResponse>> getTicketsByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(ticketService.getTicketsByEvent(eventId));
    }
}