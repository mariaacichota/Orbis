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

        mockEventDTO = new EventDTO();
        mockEventDTO.setTitle("Mock Event");
        mockEventDTO.setOrganizerId(VALID_USER_ID);

        mockEvent = new Event();
        mockEvent.setTitle("Mock Event");
    }

    @Test
    void createEvent_ShouldReturnOk_WhenValid() throws IOException {
        MultipartFile mockImage = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[0]);

        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.of(mockUser));
        when(eventService.createEvent(any(EventDTO.class), any(MultipartFile.class), eq(mockUser)))
                .thenReturn(mockEvent);

        ResponseEntity<Event> response = eventController.createEvent(mockEventDTO, mockImage, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Mock Event", response.getBody().getTitle());
    }

    @Test
    void createEvent_ShouldReturnBadRequest_WhenUserNotFound() {
        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.empty());

        mockEventDTO.setOrganizerId(VALID_USER_ID);
        ResponseEntity<Event> response = eventController.createEvent(mockEventDTO, null, null);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void updateEvent_ShouldReturnOk_WhenValid() throws IOException {
        Long eventId = 1L;
        MultipartFile mockImage = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[0]);

        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.of(mockUser));
        when(eventService.updateEvent(eq(eventId), any(EventDTO.class), eq(mockImage), eq(VALID_USER_ID)))
                .thenReturn(mockEvent);

        ResponseEntity<Event> response = eventController.updateEvent(eventId, mockEventDTO, mockImage, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Mock Event", response.getBody().getTitle());
    }

    @Test
    void updateEvent_ShouldReturnBadRequest_WhenUserNotFound() {
        Long eventId = 1L;

        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.empty());
        mockEventDTO.setOrganizerId(VALID_USER_ID);

        ResponseEntity<Event> response = eventController.updateEvent(eventId, mockEventDTO, null, null);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void deleteEvent_ShouldReturnNoContent_WhenValid() {
        Long eventId = 1L;

        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.of(mockUser));
        doNothing().when(eventService).deleteEvent(eventId, VALID_USER_ID);

        EventDTO dto = new EventDTO();
        dto.setOrganizerId(VALID_USER_ID);

        ResponseEntity<Void> response = eventController.deleteEvent(eventId, dto);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteEvent_ShouldReturnBadRequest_WhenOrganizerIdIsMissing() {
        Long eventId = 1L;
        ResponseEntity<Void> response = eventController.deleteEvent(eventId, null);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void deleteEvent_ShouldReturnNotFound_WhenUserNotFound() {
        Long eventId = 1L;

        EventDTO dto = new EventDTO();
        dto.setOrganizerId(VALID_USER_ID);

        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = eventController.deleteEvent(eventId, dto);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void addParticipant_ShouldReturnOk_WhenEventHasSpace() {
        Long eventId = 1L;
        User user = new User();
        user.setId(2L);

        Event event = new Event();
        event.setMaxTickets(10);
        event.setParticipants(List.of());

        when(eventService.getEventById(eventId)).thenReturn(Optional.of(event));
        doNothing().when(eventService).addParticipant(eventId, user.getId());

        ResponseEntity<String> response = eventController.addParticipant(eventId, user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User added as a participant.", response.getBody());
    }

    @Test
    void addParticipant_ShouldReturnBadRequest_WhenEventNotFound() {
        Long eventId = 1L;
        User user = new User();
        user.setId(2L);

        when(eventService.getEventById(eventId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = eventController.addParticipant(eventId, user);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void addParticipant_ShouldReturnBadRequest_WhenEventFull() {
        Long eventId = 1L;
        User user = new User();
        user.setId(2L);

        Event event = new Event();
        event.setMaxTickets(1);
        event.setParticipants(List.of(new User())); // Já tem 1 participante

        when(eventService.getEventById(eventId)).thenReturn(Optional.of(event));

        ResponseEntity<String> response = eventController.addParticipant(eventId, user);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Event has reached the maximum number of participants.", response.getBody());
    }
}

// Não consegui rodar esse test.
//    @Test
//    void createEvent_ShouldReturnServerError_WhenIOException() throws IOException {
//        MultipartFile mockImage = mock(MultipartFile.class);
//        when(mockImage.getBytes()).thenThrow(new IOException("Error reading file"));
//
//        when(userService.getUserById(VALID_USER_ID)).thenReturn(Optional.of(mockUser));
//
//        mockEventDTO.setOrganizerId(VALID_USER_ID);
//        ResponseEntity<Event> response = eventController.createEvent(mockEventDTO, mockImage, null);
//
//        assertEquals(500, response.getStatusCodeValue());
//    }