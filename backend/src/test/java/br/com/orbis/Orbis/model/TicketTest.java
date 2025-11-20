package br.com.orbis.Orbis.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void shouldCalculateCorrectPriceForEachType() {
        Event event = new Event();
        event.setBaseTicketPrice(100.0);

        Ticket full = new Ticket(TicketType.FULL, event, 1L);
        Ticket half = new Ticket(TicketType.HALF, event, 2L);
        Ticket vip = new Ticket(TicketType.VIP, event, 3L);

        assertEquals(100.0, full.getPrice());
        assertEquals(50.0, half.getPrice());
        assertEquals(200.0, vip.getPrice());
    }
}
