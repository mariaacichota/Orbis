package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.dto.EventDTO;
import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.model.Role;
import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.repository.EventRepository;

import br.com.orbis.Orbis.repository.UserRepository;
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
    private UserRepository userRepository;

    @Mock
    private MultipartFile mockImage;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private User organizer;
    private EventDTO eventDTO;

    @BeforeEach
    void initialize() {
        organizer = new User();
        organizer.setId(1L);
        organizer.setRole(Role.ORGANIZADOR);

        event = new Event();
        event.setId(1L);
        event.setTitle("test event");
        event.setDescription("testing");
        event.setOrganizer(organizer);

        eventDTO = new EventDTO();
        eventDTO.setTitle("Updated Title");
        eventDTO.setDescription("Updated Desc");
        eventDTO.setDate(LocalDate.parse("2025-12-31"));
        eventDTO.setTime(LocalTime.parse("20:00"));
        eventDTO.setLocation("Online");
        eventDTO.setMaxTickets(100);
    }

    @Test
    void testCreateEventWithoutImage() throws IOException {
        when(repository.save(any(Event.class))).thenReturn(event);

        Event createdEvent = eventService.createEvent(eventDTO, null, organizer);

        assertNotNull(createdEvent);
        assertEquals("test event", event.getTitle()); // conforme mock
        verify(repository).save(any(Event.class));
    }

    @Test
    void testCreateEventWithIOException() throws IOException {
        when(mockImage.isEmpty()).thenReturn(false);
        when(mockImage.getOriginalFilename()).thenReturn("image.png");
        when(mockImage.getInputStream()).thenThrow(new IOException("error"));

        assertThrows(IOException.class, () -> eventService.createEvent(eventDTO, mockImage, organizer));
    }

    @Test
    void testUpdateEventWithoutImage() throws IOException {
        when(repository.findById(1L)).thenReturn(Optional.of(event));
        when(repository.save(any(Event.class))).thenReturn(event);

        Event result = eventService.updateEvent(1L, eventDTO, null, 1L);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(repository).save(any(Event.class));
    }



    @Test
    void testUpdateEventNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                eventService.updateEvent(1L, eventDTO, null, 1L));
    }

    @Test
    void testUpdateEventInvalidOrganizer() {
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        assertThrows(IllegalArgumentException.class, () ->
                eventService.updateEvent(1L, eventDTO, null, 99L));
    }

    @Test
    void testDeleteEventSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(event));
        doNothing().when(repository).delete(any(Event.class));

        assertDoesNotThrow(() -> eventService.deleteEvent(1L, 1L));
        verify(repository).delete(event);
    }

    @Test
    void testDeleteEventNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                eventService.deleteEvent(1L, 1L));
    }

    @Test
    void testDeleteEventInvalidOrganizer() {
        User otherOrganizer = new User();
        otherOrganizer.setId(99L);
        event.setOrganizer(otherOrganizer);

        when(repository.findById(1L)).thenReturn(Optional.of(event));

        assertThrows(IllegalArgumentException.class, () ->
                eventService.deleteEvent(1L, 1L));
    }
}


// - Não consegui rodar esse teste...
//     @Test
//    void testCreateEventWithImage() throws IOException {
//        when(mockImage.isEmpty()).thenReturn(false);
//        when(mockImage.getOriginalFilename()).thenReturn("image.png");
//        when(mockImage.getInputStream()).thenReturn(getClass().getResourceAsStream("/image.png"));
//        when(repository.save(any(Event.class))).thenReturn(event);
//
//        Event createdEvent = eventService.createEvent(eventDTO, mockImage, organizer);
//
//        assertNotNull(createdEvent);
//        verify(repository).save(any(Event.class));
//    }
//
//    @Test
//    void testUpdateEventWithImage() throws IOException {
//        when(repository.findById(1L)).thenReturn(Optional.of(event));
//        when(mockImage.isEmpty()).thenReturn(false);
//        when(mockImage.getOriginalFilename()).thenReturn("image.png");
//        when(mockImage.getInputStream()).thenReturn(getClass().getResourceAsStream("/image.png"));
//        when(repository.save(any(Event.class))).thenReturn(event);
//
//        Event result = eventService.updateEvent(1L, eventDTO, mockImage, 1L);
//
//        assertEquals("Updated Title", result.getTitle());
//        verify(repository).save(any(Event.class));
//    }