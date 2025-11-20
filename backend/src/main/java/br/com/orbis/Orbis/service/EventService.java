package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.dto.EventDTO;
import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository repository;

    private static final String UPLOAD_DIR = "uploads/";

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Event createEvent(EventDTO eventDto, MultipartFile image) throws IOException {

        if (image != null && !image.isEmpty()) {
            String imagePath = UPLOAD_DIR + image.getOriginalFilename();
            Files.copy(image.getInputStream(), Paths.get(imagePath));
            eventDto.setImageUrl(imagePath);
        }

        Event event = new Event();
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setDate(eventDto.getDate());
        event.setTime(eventDto.getTime());
        event.setLocation(eventDto.getLocation());
        event.setMaxTickets(eventDto.getMaxTickets());
        event.setOrganizerId(eventDto.getOrganizerId());
        event.setBaseTicketPrice(eventDto.getBaseTicketPrice());

        return repository.save(event);
    }

    public List<Event> listEvents() {
        return repository.findAll();
    }

    public List<Event> listEventsByOrganizer(Long organizerId) {
        return repository.findByOrganizerId(organizerId);
    }

    public Optional<Event> getEventById(Long eventId) { return repository.findById(eventId); }

    public List<Event> searchByCategoryAndTag(String category, String tag) {
        return repository.findByCategoryAndTag(category, tag);
    }

    public List<Event> searchEventsWithFilters(String title, String location, LocalDate startDate, LocalDate endDate) {
        Specification<Event> spec = Specification
                .where(EventSpecifications.hasTitle(title))
                .and(EventSpecifications.hasLocation(location))
                .and(EventSpecifications.isAfterDate(startDate))
                .and(EventSpecifications.isBeforeDate(endDate));

        return repository.findAll(spec);
    }

    public Event updateEvent(Long eventId, EventDTO eventDto, MultipartFile image) throws IOException {
        Optional<Event> existingEvent = repository.findById(eventId);
        if (existingEvent.isEmpty()) {
            throw new IllegalArgumentException("Event not found");
        }
        Event eventToUpdate = existingEvent.get();

        if (!eventToUpdate.getOrganizerId().equals(eventDto.getOrganizerId())) {
            throw new IllegalArgumentException("Only the event organizer can update this event");
        }

        eventToUpdate.setTitle(eventDto.getTitle());
        eventToUpdate.setDescription(eventDto.getDescription());
        eventToUpdate.setDate(eventDto.getDate());
        eventToUpdate.setTime(eventDto.getTime());
        eventToUpdate.setLocation(eventDto.getLocation());
        eventToUpdate.setMaxTickets(eventDto.getMaxTickets());
        eventToUpdate.setBaseTicketPrice(eventDto.getBaseTicketPrice());

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
