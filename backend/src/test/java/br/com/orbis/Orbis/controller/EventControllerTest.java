package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.dto.EventDTO;
import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    private static final Long VALID_ORGANIZER_ID = 1L;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private Event mockEvent;
    private EventDTO mockEventDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockEvent = new Event();
        mockEvent.setId(1L);
        mockEvent.setTitle("Test Event");
        mockEvent.setMaxTickets(10);
        mockEvent.setOrganizerId(VALID_ORGANIZER_ID);

        mockEventDTO = new EventDTO();
        mockEventDTO.setTitle("Test Event");
        mockEventDTO.setOrganizerId(VALID_ORGANIZER_ID);
        mockEventDTO.setMaxTickets(10);
    }

    @Test
    void createEventSuccess() throws IOException {
        when(eventService.createEvent(any(EventDTO.class), any())).thenReturn(mockEvent);

        ResponseEntity<Event> response = eventController.createEvent(mockEventDTO, null, null);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(mockEvent.getId(), response.getBody().getId());
    }

    @Test
    void createEventWithImageSuccess() throws IOException {
        MultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
        when(eventService.createEvent(any(EventDTO.class), eq(image))).thenReturn(mockEvent);

        ResponseEntity<Event> response = eventController.createEvent(mockEventDTO, image, null);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void createEventNoOrganizerIdProvided() {
        EventDTO invalidDTO = new EventDTO();
        invalidDTO.setTitle("Test");
        invalidDTO.setOrganizerId(null);

        ResponseEntity<Event> response = eventController.createEvent(invalidDTO, null, null);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void createEventIOExceptionHandling() throws IOException {
        when(eventService.createEvent(any(EventDTO.class), any())).thenThrow(new IOException());

        ResponseEntity<Event> response = eventController.createEvent(mockEventDTO, null, null);

        assertEquals(500, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void listEventsSuccess() {
        List<Event> events = List.of(mockEvent);
        when(eventService.listEvents()).thenReturn(events);

        ResponseEntity<List<Event>> response = eventController.listEvents();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void searchEventsSuccess() {
        List<Event> events = List.of(mockEvent);
        when(eventService.searchByCategoryAndTag("category", "tag")).thenReturn(events);

        ResponseEntity<List<Event>> response = eventController.searchEvents("category", "tag");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getEventByIdSuccess() {
        when(eventService.getEventById(1L)).thenReturn(Optional.of(mockEvent));

        ResponseEntity<Event> response = eventController.getEventById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void getEventByIdNotFound() {
        when(eventService.getEventById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Event> response = eventController.getEventById(1L);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void deleteEventSuccess() {
        EventDTO deleteEventDTO = new EventDTO();
        deleteEventDTO.setOrganizerId(VALID_ORGANIZER_ID);

        doNothing().when(eventService).deleteEvent(1L, VALID_ORGANIZER_ID);

        ResponseEntity<Void> response = eventController.deleteEvent(1L, deleteEventDTO);

        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void deleteEventNoOrganizerIdProvided() {
        ResponseEntity<Void> response = eventController.deleteEvent(1L, null);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void listEventsByOrganizerSuccess() {
        List<Event> events = List.of(mockEvent);
        when(eventService.listEventsByOrganizer(VALID_ORGANIZER_ID)).thenReturn(events);

        ResponseEntity<List<Event>> response = eventController.listEventsByOrganizer(VALID_ORGANIZER_ID);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void updateEventSuccess() throws IOException {
        when(eventService.updateEvent(eq(1L), any(EventDTO.class), any())).thenReturn(mockEvent);

        ResponseEntity<Event> response = eventController.updateEvent(1L, mockEventDTO, null, null);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void updateEventNoOrganizerIdProvided() {
        EventDTO invalidDTO = new EventDTO();
        invalidDTO.setTitle("Test");
        invalidDTO.setOrganizerId(null);

        ResponseEntity<Event> response = eventController.updateEvent(1L, invalidDTO, null, null);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void updateEventIOExceptionHandling() throws IOException {
        when(eventService.updateEvent(eq(1L), any(EventDTO.class), any()))
            .thenThrow(new IOException());

        ResponseEntity<Event> response = eventController.updateEvent(1L, mockEventDTO, null, null);

        assertEquals(500, response.getStatusCode().value());
    }
}
