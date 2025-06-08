package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.dto.EventDTO;
import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.model.Role;
import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.service.EventService;
import br.com.orbis.Orbis.service.UserService;
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

    private static final Long VALID_USER_ID = 1L;

    @Mock
    private EventService eventService;

    @Mock
    private UserService userService;

    @InjectMocks
    private EventController eventController;

    private Event mockEvent;
    private User mockUser;
    private EventDTO mockEventDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setId(VALID_USER_ID);
        mockUser.setRole(Role.ORGANIZADOR);

        mockEvent = new Event();
        mockEvent.setId(1L);
        mockEvent.setTitle("Test Event");
        mockEvent.setMaxTickets(10);
        mockEvent.setOrganizer(mockUser);

        mockEventDTO = new EventDTO();
        mockEventDTO.setTitle("Test Event");
        mockEventDTO.setOrganizerId(VALID_USER_ID);
        mockEventDTO.setMaxTickets(10);
    }

    @Test
    void createEventSuccess() throws IOException {
        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.of(mockUser));
        when(eventService.createEvent(any(EventDTO.class), any(), any(User.class))).thenReturn(mockEvent);

        ResponseEntity<Event> response = eventController.createEvent(mockEventDTO, null, null);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(mockEvent.getId(), response.getBody().getId());
    }

    @Test
    void createEventWithImageSuccess() throws IOException {
        MultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.of(mockUser));
        when(eventService.createEvent(any(EventDTO.class), eq(image), any(User.class))).thenReturn(mockEvent);

        ResponseEntity<Event> response = eventController.createEvent(mockEventDTO, image, null);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void createEventUserNotFound() {
        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.empty());

        ResponseEntity<Event> response = eventController.createEvent(mockEventDTO, null, null);

        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void createEventIOExceptionHandling() throws IOException {
        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.of(mockUser));
        when(eventService.createEvent(any(EventDTO.class), any(), any(User.class))).thenThrow(new IOException());

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
        deleteEventDTO.setOrganizerId(VALID_USER_ID);

        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.of(mockUser));
        doNothing().when(eventService).deleteEvent(1L, VALID_USER_ID);

        ResponseEntity<Void> response = eventController.deleteEvent(1L, deleteEventDTO);

        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void deleteEventUserNotFound() {
        EventDTO deleteEventDTO = new EventDTO();
        deleteEventDTO.setOrganizerId(VALID_USER_ID);

        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = eventController.deleteEvent(1L, deleteEventDTO);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void deleteEventNoOrganizerIdProvided() {
        ResponseEntity<Void> response = eventController.deleteEvent(1L, null);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void addParticipantSuccess() {
        when(eventService.getEventById(1L)).thenReturn(Optional.of(mockEvent));
        doNothing().when(eventService).addParticipant(1L, VALID_USER_ID);

        ResponseEntity<String> response = eventController.addParticipant(1L, mockUser);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("User added as a participant.", response.getBody());
    }

    @Test
    void addParticipantEventNotFound() {
        when(eventService.getEventById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = eventController.addParticipant(1L, mockUser);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Event not found", response.getBody());
    }

    @Test
    void addParticipantEventFull() {
        Event fullEvent = new Event();
        fullEvent.setMaxTickets(1);
        User existingParticipant = new User();
        existingParticipant.setId(2L);
        fullEvent.getParticipants().add(existingParticipant);

        when(eventService.getEventById(1L)).thenReturn(Optional.of(fullEvent));

        ResponseEntity<String> response = eventController.addParticipant(1L, mockUser);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Event has reached the maximum number of participants.", response.getBody());
    }

    @Test
    void listEventsByOrganizerSuccess() {
        List<Event> events = List.of(mockEvent);
        when(eventService.listEventsByOrganizer(VALID_USER_ID)).thenReturn(events);

        ResponseEntity<List<Event>> response = eventController.listEventsByOrganizer(VALID_USER_ID);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void updateEventSuccess() throws IOException {
        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.of(mockUser));
        when(eventService.updateEvent(eq(1L), any(EventDTO.class), isNull(), eq(VALID_USER_ID))).thenReturn(mockEvent);

        ResponseEntity<Event> response = eventController.updateEvent(1L, mockEventDTO, null, null);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void updateEventUserNotFound() {
        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.empty());

        ResponseEntity<Event> response = eventController.updateEvent(1L, mockEventDTO, null, null);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void updateEventIOExceptionHandling() throws IOException {
        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.of(mockUser));
        when(eventService.updateEvent(eq(1L), any(EventDTO.class), isNull(), eq(VALID_USER_ID)))
            .thenThrow(new IOException());

        ResponseEntity<Event> response = eventController.updateEvent(1L, mockEventDTO, null, null);

        assertEquals(500, response.getStatusCode().value());
    }
}

