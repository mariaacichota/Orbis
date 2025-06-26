package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.model.TicketType;
import br.com.orbis.Orbis.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketControllerTest {

    @InjectMocks
    private TicketController ticketController;

    @Mock
    private TicketService ticketService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnSuccessWhenTicketSold() {
        Long eventId = 1L;
        Long userId = 2L;
        TicketType type = TicketType.HALF;

        when(ticketService.processTicketSale(eventId, userId, type)).thenReturn(true);

        ResponseEntity<String> response = ticketController.sellTicket(eventId, userId, type);

        assertEquals("Ingresso vendido com sucesso!", response.getBody());
    }

    @Test
    void shouldReturnBadRequestWhenCapacityIsFull() {
        Long eventId = 1L;
        Long userId = 2L;
        TicketType type = TicketType.FULL;

        when(ticketService.processTicketSale(eventId, userId, type))
                .thenThrow(new IllegalStateException("Capacidade máxima atingida."));

        ResponseEntity<String> response = ticketController.sellTicket(eventId, userId, type);

        assertTrue(Objects.requireNonNull(response.getBody()).contains("Capacidade máxima atingida."));
    }

    @Test
    void shouldReturnServerErrorWhenUnexpectedExceptionOccurs() {
        Long eventId = 1L;
        Long userId = 2L;
        TicketType type = TicketType.VIP;

        when(ticketService.processTicketSale(eventId, userId, type))
                .thenThrow(new RuntimeException("Falha inesperada"));

        ResponseEntity<String> response = ticketController.sellTicket(eventId, userId, type);

        assertTrue(Objects.requireNonNull(response.getBody()).contains("Erro inesperado"));
    }
}