package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.model.Ticket;
import br.com.orbis.Orbis.model.TicketType;
import br.com.orbis.Orbis.repository.EventRepository;
import br.com.orbis.Orbis.repository.TicketRepository;
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

    @InjectMocks
    private TicketService ticketService;

    private Event mockEvent;
    private static final Long EVENT_ID = 1L;
    private static final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockEvent = new Event();
        mockEvent.setId(EVENT_ID);
        mockEvent.setMaxTickets(10);
    }

    @Test
    void processTicketSaleSuccess() {
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(mockEvent));
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
    void processTicketSaleEventFull() {
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(mockEvent));
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
        when(ticketRepository.countByEventId(EVENT_ID)).thenReturn(0L);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket());

        boolean resultFull = ticketService.processTicketSale(EVENT_ID, USER_ID, TicketType.FULL);
        boolean resultHalf = ticketService.processTicketSale(EVENT_ID, USER_ID, TicketType.HALF);

        assertTrue(resultFull);
        assertTrue(resultHalf);
        verify(ticketRepository, times(2)).save(any(Ticket.class));
    }
}
