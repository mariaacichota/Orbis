package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.dto.EventDTO;
import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository repository;

    @Mock
    private MultipartFile mockImage;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private EventDTO eventDTO;
    private static final Long ORGANIZER_ID = 1L;

    @BeforeEach
    void initialize() {
        event = new Event();
        event.setId(1L);
        event.setTitle("test event");
        event.setDescription("testing");
        event.setOrganizerId(ORGANIZER_ID);

        eventDTO = new EventDTO();
        eventDTO.setTitle("Updated Title");
        eventDTO.setDescription("Updated Desc");
        eventDTO.setDate(LocalDate.parse("2025-12-31"));
        eventDTO.setTime(LocalTime.parse("20:00"));
        eventDTO.setLocation("Online");
        eventDTO.setMaxTickets(100);
        eventDTO.setOrganizerId(ORGANIZER_ID);
    }

    @Test
    void testCreateEventWithoutImage() throws IOException {
        when(repository.save(any(Event.class))).thenReturn(event);

        Event createdEvent = eventService.createEvent(eventDTO, null);

        assertNotNull(createdEvent);
        assertEquals("test event", event.getTitle());
        verify(repository).save(any(Event.class));
    }

    @Test
    void testCreateEventWithIOException() throws IOException {
        when(mockImage.isEmpty()).thenReturn(false);
        when(mockImage.getOriginalFilename()).thenReturn("image.png");
        when(mockImage.getInputStream()).thenThrow(new IOException("error"));

        assertThrows(IOException.class, () -> eventService.createEvent(eventDTO, mockImage));
    }

    @Test
    void testUpdateEventWithoutImage() throws IOException {
        when(repository.findById(1L)).thenReturn(Optional.of(event));
        when(repository.save(any(Event.class))).thenReturn(event);

        Event result = eventService.updateEvent(1L, eventDTO, null);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(repository).save(any(Event.class));
    }

    @Test
    void testUpdateEventNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                eventService.updateEvent(1L, eventDTO, null));
    }

    @Test
    void testUpdateEventInvalidOrganizer() {
        when(repository.findById(1L)).thenReturn(Optional.of(event));
        EventDTO otherOrganizerDTO = new EventDTO();
        otherOrganizerDTO.setOrganizerId(99L);

        assertThrows(IllegalArgumentException.class, () ->
                eventService.updateEvent(1L, otherOrganizerDTO, null));
    }

    @Test
    void testDeleteEventSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(event));
        doNothing().when(repository).delete(any(Event.class));

        assertDoesNotThrow(() -> eventService.deleteEvent(1L, ORGANIZER_ID));
        verify(repository).delete(event);
    }

    @Test
    void testDeleteEventNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                eventService.deleteEvent(1L, ORGANIZER_ID));
    }

    @Test
    void testDeleteEventInvalidOrganizer() {
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        assertThrows(IllegalArgumentException.class, () ->
                eventService.deleteEvent(1L, 99L));
    }

    @Test
    void testListEventsSuccess() {
        List<Event> events = List.of(event);
        when(repository.findAll()).thenReturn(events);

        List<Event> result = eventService.listEvents();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void testListEventsByOrganizerSuccess() {
        List<Event> events = List.of(event);
        when(repository.findByOrganizerId(ORGANIZER_ID)).thenReturn(events);

        List<Event> result = eventService.listEventsByOrganizer(ORGANIZER_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findByOrganizerId(ORGANIZER_ID);
    }

    @Test
    void testGetEventByIdSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        Optional<Event> result = eventService.getEventById(1L);

        assertTrue(result.isPresent());
        assertEquals("test event", result.get().getTitle());
    }

    @Test
    void testGetEventByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<Event> result = eventService.getEventById(1L);

        assertFalse(result.isPresent());
    }
}
