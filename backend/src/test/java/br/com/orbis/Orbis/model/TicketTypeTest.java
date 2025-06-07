package br.com.orbis.Orbis.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TicketTypeTest {

    private static final double DELTA = 0.001;
    private static final double BASE_PRICE = 50.0;

    @Test
    void testCalculatePrice() {
        assertEquals(50.0, TicketType.FULL.calculatePrice(BASE_PRICE), DELTA);
        assertEquals(25.0, TicketType.HALF.calculatePrice(BASE_PRICE), DELTA);
        assertEquals(100.0, TicketType.VIP.calculatePrice(BASE_PRICE), DELTA);
    }

    @Test
    void testCalculatePriceWithDifferentBasePrice() {
        double eventPrice = 100.0;
        assertEquals(100.0, TicketType.FULL.calculatePrice(eventPrice), DELTA);
        assertEquals(50.0, TicketType.HALF.calculatePrice(eventPrice), DELTA);
        assertEquals(200.0, TicketType.VIP.calculatePrice(eventPrice), DELTA);
    }

    @Test
    void testCalculatePriceWithZeroPrice() {
        assertEquals(0.0, TicketType.FULL.calculatePrice(0.0), DELTA);
        assertEquals(0.0, TicketType.HALF.calculatePrice(0.0), DELTA);
        assertEquals(0.0, TicketType.VIP.calculatePrice(0.0), DELTA);
    }
}
