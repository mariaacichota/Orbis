package br.com.orbis.Orbis.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void shouldCalculateCorrectPriceForEachType() {
        Event event = new Event();
        event.setBaseTicketPrice(100.0);

        Ticket full = new Ticket(TicketType.FULL, event, new User());
        Ticket half = new Ticket(TicketType.HALF, event, new User());
        Ticket vip = new Ticket(TicketType.VIP, event, new User());

        assertEquals(100.0, full.getPrice());
        assertEquals(50.0, half.getPrice());
        assertEquals(200.0, vip.getPrice());
    }
}
