package br.com.orbis.Orbis.model;

import br.com.orbis.Orbis.controller.EventController;
import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.service.EventService;



import static org.mockito.Mockito.*;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private Event mockEvent;

    @BeforeEach
    void  initializeMocks() {
        MockitoAnnotations.openMocks(this);

        mockEvent = new Event();
    }

    @Test
    void testCreateEvent() throws Exception {
        MultipartFile mockImage = new MockMultipartFile("image.jpg", new byte[0]);

        when(eventService.createEvent(any(Event.class), any(MultipartFile.class)))
                .thenReturn(mockEvent);

        ResponseEntity<Event> response = eventController.createEvent(mockEvent, mockImage, null);

        assertNotNull(response.getBody());
    }

    @Test
    void testListEvents() {
        List<Event> eventList = Arrays.asList(mockEvent, new Event());

        when(eventService.listEvents()).thenReturn(eventList);

        ResponseEntity<?> response = eventController.listEvents();
        assertEquals(2, ((List<?>) response.getBody()).size());
    }

    @Test
    void testListEventsByOrganizer() {
        Long organizerId = 1L;
        List<Event> eventList = Arrays.asList(mockEvent);

        when(eventService.listEventsByOrganizer(organizerId)).thenReturn(eventList);

        ResponseEntity<?> response = eventController.listEventsByOrganizer(organizerId);
        assertEquals(1, ((List<?>) response.getBody()).size());
    }

    @Test
    void testUpdateEvent() throws Exception {
        Long eventId = 1L;
        Long organizerId = 1L;
        MultipartFile mockImage = new MockMultipartFile("image.jpg", new byte[0]);

        Event originalEvent = new Event();
        originalEvent.setTitle("old event title");

        Event updatedEvent = new Event();
        updatedEvent.setTitle("updated event title");

        when(eventService.updateEvent(eq(eventId), any(Event.class), any(MultipartFile.class), eq(organizerId)))
                .thenReturn(updatedEvent);

        ResponseEntity<?> response = eventController.updateEvent(eventId, updatedEvent, mockImage, organizerId);
        Event result = (Event) response.getBody();
        assertEquals("updated event title", result.getTitle());
    }




    @Test
    void testDeleteEvent() {
        Long eventId = 1L;
        Long organizerId = 1L;

        doNothing().when(eventService).deleteEvent(eventId, organizerId);
        eventController.deleteEvent(eventId, organizerId);
    }


    @Test
    void testCreateEventWithIOException() throws Exception {
        MultipartFile mockImage = new MockMultipartFile("image.jpg", new byte[0]);

        when(eventService.createEvent(any(Event.class), any(MultipartFile.class)))
                .thenThrow(new IOException());

        ResponseEntity<Event> response = eventController.createEvent(mockEvent, mockImage, null);

        assertEquals(400, response.getStatusCodeValue());
    }
    @Test
    void testUpdateEventWithNotFoundException() throws Exception {
        Long eventId = 1L;
        Long organizerId = 1L;
        MultipartFile mockImage = new MockMultipartFile("image.jpg", new byte[0]);

        when(eventService.updateEvent(eq(eventId), any(Event.class), any(MultipartFile.class), eq(organizerId)))
                .thenThrow(new IllegalArgumentException());

        ResponseEntity<?> response = eventController.updateEvent(eventId, mockEvent, mockImage, organizerId);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testUpdateEventWithSecurityException() throws Exception {
        when(eventService.updateEvent(anyLong(), any(Event.class), any(MultipartFile.class), anyLong()))
                .thenThrow(new SecurityException());

        ResponseEntity<?> response = eventController.updateEvent(1L, mockEvent, new MockMultipartFile("image.jpg", new byte[0]), 1L);

        assertEquals(403, response.getStatusCodeValue());
    }
    @Test
    void testUpdateEventWithIOException() throws Exception {
        when(eventService.updateEvent(anyLong(), any(Event.class), any(MultipartFile.class), anyLong()))
                .thenThrow(new IOException());

        ResponseEntity<?> response = eventController.updateEvent(1L, mockEvent, new MockMultipartFile("image.jpg", new byte[0]), 1L);

        assertEquals(400, response.getStatusCodeValue());
    }


    @Test
    void testDeleteEventWithSecurityException() {
        Long eventId = 1L;
        Long organizerId = 1L;

        doThrow(new SecurityException()).when(eventService).deleteEvent(eventId, organizerId);

        ResponseEntity<?> response = eventController.deleteEvent(eventId, organizerId);

        assertEquals(403, response.getStatusCodeValue());
    }


}

