package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(
            @Valid @RequestPart("event") Event event,
            @RequestPart("image") MultipartFile image) {
        try {
            Event createdEvent = service.createEvent(event, image);
            return ResponseEntity.ok(createdEvent);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
