package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.dto.EventDTO;
import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.model.Role;
import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.service.EventService;
import br.com.orbis.Orbis.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService service;
    private final UserService userService;

    public EventController(EventService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping(consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<Event> createEvent(
            @Valid @RequestPart(value = "event", required = false) EventDTO event,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestBody(required = false) EventDTO eventBody) {

        log.info("[POST] /events - Criando evento");
        try {
            EventDTO eventToCreate = event != null ? event : eventBody;
            User user = userService.getUserById(eventToCreate.getOrganizerId())
                    .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_MESSAGE));
            log.info("Organizador encontrado: {}", user.getEmail());
            Event createdEvent = service.createEvent(eventToCreate, image, user);
            log.info("Evento criado com sucesso: {}", createdEvent.getId());
            return ResponseEntity.ok(createdEvent);
        } catch (IOException e) {
            log.error("Erro ao processar imagem do evento", e);
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            log.warn("Falha ao criar evento: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Event>> listEvents() {
        return ResponseEntity.ok(service.listEvents());
    }

    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<Event>> listEventsByOrganizer(@PathVariable Long organizerId) {
        return ResponseEntity.ok(service.listEventsByOrganizer(organizerId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag) {
        return ResponseEntity.ok(service.searchByCategoryAndTag(category, tag));
    }

    @PutMapping(value = "/{eventId}", consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestPart(value = "event", required = false) EventDTO event,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestBody(required = false) EventDTO eventBody) {

        try {
            EventDTO eventToUpdate = event != null ? event : eventBody;

            // Verifica se o organizerId está no corpo da requisição
            User user = userService.getUserById(eventToUpdate.getOrganizerId())
                    .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_MESSAGE));

            Event updatedEvent = service.updateEvent(eventId, eventToUpdate, image, user.getId());
            return ResponseEntity.ok(updatedEvent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long eventId,
            @RequestBody(required = false) EventDTO eventBody) {

        try {
            // Verifica se o organizerId está no corpo da requisição
            if (eventBody == null || eventBody.getOrganizerId() == null) {
                return ResponseEntity.badRequest().build();
            }

            User user = userService.getUserById(eventBody.getOrganizerId())
                    .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_MESSAGE));

            service.deleteEvent(eventId, user.getId());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        return service.getEventById(eventId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{eventId}/participants")
    public ResponseEntity<String> addParticipant(@PathVariable Long eventId, @RequestBody User user) {
        try {

            // Verifica se o evento existe
            Event event = service.getEventById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));

            // Verifica se o evento atingiu o limite de participantes
            if (event.getParticipants().size() >= event.getMaxTickets()) {
                return ResponseEntity.status(400).body("Event has reached the maximum number of participants.");
            }

            // Adiciona o participante ao evento
            service.addParticipant(eventId, user.getId());

            return ResponseEntity.ok("User added as a participant.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
