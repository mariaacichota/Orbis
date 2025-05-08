package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository repository;
    private final String UPLOAD_DIR = "uploads/";

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Event createEvent(Event event, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String imagePath = UPLOAD_DIR + image.getOriginalFilename();
            Files.copy(image.getInputStream(), Paths.get(imagePath));
            event.setImageUrl(imagePath);
        }
        return repository.save(event);
    }

    public List<Event> listEvents() {
        return repository.findAll();
    }

    public List<Event> listEventsByOrganizer(Long organizerId) {
        return repository.findByOrganizerId(organizerId);
    }

    public List<Event> searchByCategoryAndTag(String category, String tag) {
        return repository.findByCategoryAndTag(category, tag);
    }

    public Event updateEvent(Long eventId, Event event, MultipartFile image, Long currentOrganizerId) throws IOException {
        Optional<Event> existingEvent = repository.findById(eventId);
        if (existingEvent.isEmpty()) {
            throw new IllegalArgumentException("Event not found");
        }
        Event eventToUpdate = existingEvent.get();

        if (!eventToUpdate.getOrganizerId().equals(currentOrganizerId)) {
            throw new IllegalArgumentException("Only the event organizer can update this event");
        }

        eventToUpdate.setTitle(event.getTitle());
        eventToUpdate.setDescription(event.getDescription());
        eventToUpdate.setDate(event.getDate());
        eventToUpdate.setTime(event.getTime());
        eventToUpdate.setLocation(event.getLocation());
        eventToUpdate.setSpeakers(event.getSpeakers());
        eventToUpdate.setActivities(event.getActivities());
        eventToUpdate.setMaxTickets(event.getMaxTickets());

        if (image != null && !image.isEmpty()) {
            String imagePath = UPLOAD_DIR + image.getOriginalFilename();
            Files.copy(image.getInputStream(), Paths.get(imagePath));
            eventToUpdate.setImageUrl(imagePath);
        }

        return repository.save(eventToUpdate);
    }

    public void deleteEvent(Long eventId, Long organizerId) {
        Optional<Event> existingEvent = repository.findById(eventId);
        if (existingEvent.isEmpty()) {
            throw new IllegalArgumentException("Event not found");
        }
        Event eventToDelete = existingEvent.get();

        if (!eventToDelete.getOrganizerId().equals(organizerId)) {
            throw new IllegalArgumentException("Only the event organizer can delete this event");
        }

        repository.delete(eventToDelete);
    }
}

