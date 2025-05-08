package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping(consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<Event> createEvent(
            @Valid @RequestPart(value = "event", required = false) Event event,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestBody(required = false) Event eventBody) {
        try {
            Event eventToCreate = event != null ? event : eventBody;
            if (eventToCreate == null) {
                return ResponseEntity.badRequest().body(null);
            }
            Event createdEvent = service.createEvent(eventToCreate, image);
            return ResponseEntity.ok(createdEvent);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Event>> listEvents() {
        List<Event> events = service.listEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<Event>> listEventsByOrganizer(@PathVariable Long organizerId) {
        List<Event> events = service.listEventsByOrganizer(organizerId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag) {
        List<Event> events = service.searchByCategoryAndTag(category, tag);
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestPart("event") Event event,
            @RequestPart("image") MultipartFile image,
            @RequestHeader("Authorization") Long organizerId) {
        try {
            Event updatedEvent = service.updateEvent(eventId, event, image, organizerId);
            return ResponseEntity.ok(updatedEvent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(null);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long eventId,
            @RequestHeader("Authorization") Long organizerId) {
        try {
            service.deleteEvent(eventId, organizerId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        }
    }
}
