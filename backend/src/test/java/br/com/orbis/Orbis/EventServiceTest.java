package br.com.orbis.Orbis.model;

import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.repository.EventRepository;
import br.com.orbis.Orbis.service.EventService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @BeforeEach
    void  initializeMocks() {
        event = new Event();
        event.setId(1L);
        event.setTitle("test event");
        event.setDescription("testing");
        event.setOrganizerId(4L);
    }


    @Test
    void testCreateEventWithoutImage() throws IOException {
        when(repository.save(any(Event.class))).thenReturn(new Event());

        Event createdEvent = eventService.createEvent(new Event(), null);

        assertNotNull(createdEvent);
    }
    @Test
    void testCreateEventWithIOException() throws IOException {
        when(mockImage.isEmpty()).thenReturn(false);
        when(mockImage.getOriginalFilename()).thenReturn("image.png");
        doThrow(new IOException("error while saving")).when(mockImage).getInputStream();

        assertThrows(IOException.class, () -> eventService.createEvent(event, mockImage));
    }

    @Test
    void testUpdateEventWithoutImage() throws IOException {
        when(repository.findById(1L)).thenReturn(Optional.of(event));
        when(repository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Event updatedEvent = new Event();
        updatedEvent.setTitle("Updated Title");

        Event result = eventService.updateEvent(1L, updatedEvent, null, 4L);
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertNull(result.getImageUrl());
    }


    @Test
    void testListEvents() {
        when(repository.findAll()).thenReturn(List.of(event));
        List<Event> events = eventService.listEvents();
        assertEquals(1, events.size());
    }

    @Test
    void testListEventsByOrganizer() {
        when(repository.findByOrganizerId(4L)).thenReturn(List.of(event));
        List<Event> events = eventService.listEventsByOrganizer(4L);
        assertEquals(1, events.size());
    }

    @Test
    void testUpdateEventSuccess() throws IOException {
        when(repository.findById(1L)).thenReturn(Optional.of(event));
        when(repository.save(any(Event.class))).thenReturn(event);

        Event updatedEvent = new Event();
        updatedEvent.setTitle("Updated Title");

        Event result = eventService.updateEvent(1L, updatedEvent, null, 4L);
        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    void testUpdateEventWithErrorNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> eventService.updateEvent(1L, event, null, 4L));
    }

    @Test
    void testUpdateEventWithInvalidOrganizer() {
        when(repository.findById(1L)).thenReturn(Optional.of(event));
        assertThrows(IllegalArgumentException.class, () -> eventService.updateEvent(1L, event, null, 99L));
    }

    @Test
    void testDeleteEventSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(event));
        doNothing().when(repository).delete(event);

        assertDoesNotThrow(() -> eventService.deleteEvent(1L, 4L));
    }

    @Test
    void testDeleteEventWithErrorNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> eventService.deleteEvent(1L, 4L));
    }

    @Test
    void testDeleteEventWithInvalidOrganizer() {
        when(repository.findById(1L)).thenReturn(Optional.of(event));
        assertThrows(IllegalArgumentException.class, () -> eventService.deleteEvent(1L, 99L));
    }
}
