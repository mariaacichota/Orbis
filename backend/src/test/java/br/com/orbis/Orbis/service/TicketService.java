package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.model.*;
import br.com.orbis.Orbis.repository.EventRepository;
import br.com.orbis.Orbis.repository.TicketRepository;
import br.com.orbis.Orbis.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSellTicketSuccessfully() {
        Long eventId = 1L;
        Long userId = 2L;
        TicketType type = TicketType.HALF;

        Event event = new Event();
        event.setId(eventId);
        event.setMaxTickets(10);
        event.setBaseTicketPrice(100.0);

        User user = new User();
        user.setId(userId);

        when(eventRepository.findById(eventId)).thenReturn(java.util.Optional.of(event));
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(ticketRepository.countByEventId(eventId)).thenReturn(3L);

        boolean result = ticketService.processTicketSale(eventId, userId, type);

        assertTrue(result);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void shouldFailWhenEventIsFull() {
        Long eventId = 1L;
        Long userId = 2L;

        Event event = new Event();
        event.setId(eventId);
        event.setMaxTickets(2);

        User user = new User();
        user.setId(userId);

        when(eventRepository.findById(eventId)).thenReturn(java.util.Optional.of(event));
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(ticketRepository.countByEventId(eventId)).thenReturn(2L);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                ticketService.processTicketSale(eventId, userId, TicketType.VIP));

        assertEquals("Não há mais tickets disponíveis para o evento.", exception.getMessage());
        verify(ticketRepository, never()).save(any(Ticket.class));
    }
}