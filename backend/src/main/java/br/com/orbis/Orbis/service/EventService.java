package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.dto.EventDTO;
import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.model.Role;
import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.repository.EventRepository;
import br.com.orbis.Orbis.repository.UserRepository;
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

    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/";

    public EventService(EventRepository repository, UserRepository userRepository) { this.repository = repository;
        this.userRepository = userRepository;
    }

    public Event createEvent(EventDTO eventDto, MultipartFile image, User user) throws IOException {

        if (user.getRole() != Role.ORGANIZADOR) {
            throw new IllegalArgumentException("Usuario não é organizador.");
        }

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
        event.setOrganizer(user);  // Define o organizador
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


    public Event updateEvent(Long eventId, EventDTO eventDto, MultipartFile image, Long currentOrganizerId) throws IOException {
        Optional<Event> existingEvent = repository.findById(eventId);
        if (existingEvent.isEmpty()) {
            throw new IllegalArgumentException("Event not found");
        }
        Event eventToUpdate = existingEvent.get();

        if (!eventToUpdate.getOrganizer().getId().equals(currentOrganizerId)) {
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

        if (eventToDelete.getOrganizer() == null || !eventToDelete.getOrganizer().getId().equals(organizerId)) {
            throw new IllegalArgumentException("Only the event organizer can delete this event");
        }

        repository.delete(eventToDelete);
    }

    public void addParticipant(Long eventId, Long userId) {
        Optional<Event> existingEvent = repository.findById(eventId);
        if (existingEvent.isEmpty()) {
            throw new IllegalArgumentException("Event not found");
        }

        Event event = existingEvent.get();


        Optional<User> existingUser = userRepository.getUserById(userId);
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = existingUser.get();

        if (user.getRole() != Role.PARTICIPANTE) {
            throw new IllegalArgumentException("User does not have the PARTICIPANTE role");
        }

        if (event.getParticipants().size() >= event.getMaxTickets()) {
            throw new IllegalArgumentException("Event has reached the maximum number of participants");
        }

        event.getParticipants().add(user);

        user.getParticipatingEvents().add(event);

        repository.save(event);

        userRepository.save(user);
    }

}
