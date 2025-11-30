package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.dto.EventDTO;
import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.service.EventService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping(consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<Event> createEvent(
            @Valid @RequestPart(value = "event", required = false) EventDTO event,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestBody(required = false) EventDTO eventBody) {

        log.info("[POST] /events - Criando evento");
        try {
            EventDTO eventToCreate = event != null ? event : eventBody;
            if (eventToCreate.getOrganizerId() == null) {
                return ResponseEntity.badRequest().body(null);
            }
            log.info("Criando evento com organizador ID: {}", eventToCreate.getOrganizerId());
            Event createdEvent = service.createEvent(eventToCreate, image);
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

    @GetMapping("/search-dynamic")
    public ResponseEntity<List<Event>> searchEventsWithFilters(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Event> events = service.searchEventsWithFilters(title, location, startDate, endDate);
        return ResponseEntity.ok(events);
    }

    @PutMapping(value = "/{eventId}", consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestPart(value = "event", required = false) EventDTO event,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestBody(required = false) EventDTO eventBody) {

        try {
            EventDTO eventToUpdate = event != null ? event : eventBody;
            if (eventToUpdate.getOrganizerId() == null) {
                return ResponseEntity.badRequest().body(null);
            }

            Event updatedEvent = service.updateEvent(eventId, eventToUpdate, image);
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
            if (eventBody == null || eventBody.getOrganizerId() == null) {
                return ResponseEntity.badRequest().build();
            }

            service.deleteEvent(eventId, eventBody.getOrganizerId());
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
}
