package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.model.Ticket;
import br.com.orbis.Orbis.model.TicketType;
import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.repository.EventRepository;
import br.com.orbis.Orbis.repository.TicketRepository;
import br.com.orbis.Orbis.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketService ticketService;

    private Event mockEvent;
    private User mockUser;
    private static final Long EVENT_ID = 1L;
    private static final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockEvent = new Event();
        mockEvent.setId(EVENT_ID);
        mockEvent.setMaxTickets(10);

        mockUser = new User();
        mockUser.setId(USER_ID);
    }

    @Test
    void processTicketSaleSuccess() {
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(mockEvent));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mockUser));
        when(ticketRepository.countByEventId(EVENT_ID)).thenReturn(0L);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket());

        boolean result = ticketService.processTicketSale(EVENT_ID, USER_ID, TicketType.FULL);

        assertTrue(result);
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void processTicketSaleEventNotFound() {
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
            ticketService.processTicketSale(EVENT_ID, USER_ID, TicketType.FULL)
        );
    }

    @Test
    void processTicketSaleUserNotFound() {
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(mockEvent));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
            ticketService.processTicketSale(EVENT_ID, USER_ID, TicketType.FULL)
        );
    }

    @Test
    void processTicketSaleEventFull() {
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(mockEvent));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mockUser));
        when(ticketRepository.countByEventId(EVENT_ID)).thenReturn(10L); // evento já está lotado

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
            ticketService.processTicketSale(EVENT_ID, USER_ID, TicketType.FULL)
        );

        assertEquals("Não há mais tickets disponíveis para o evento.", exception.getMessage());
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void processTicketSaleWithDifferentTicketTypes() {
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(mockEvent));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mockUser));
        when(ticketRepository.countByEventId(EVENT_ID)).thenReturn(0L);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket());

        for (TicketType type : TicketType.values()) {
            boolean result = ticketService.processTicketSale(EVENT_ID, USER_ID, type);
            assertTrue(result);
            verify(ticketRepository, times(1)).save(argThat(ticket ->
                ticket.getType() == type &&
                ticket.getEvent().getId().equals(EVENT_ID) &&
                ticket.getUser().getId().equals(USER_ID)
            ));
        }
    }

    @Test
    void processTicketSaleNearCapacity() {
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(mockEvent));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mockUser));
        when(ticketRepository.countByEventId(EVENT_ID)).thenReturn(9L); // apenas 1 ingresso disponível
        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket());

        boolean result = ticketService.processTicketSale(EVENT_ID, USER_ID, TicketType.FULL);

        assertTrue(result);
        verify(ticketRepository).save(any(Ticket.class));
    }
}
