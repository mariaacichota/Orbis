package br.com.orbis.Orbis.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class EventTest{


    @Test
    void eventWithoutTitle_ShouldFail() {
        Event event = new Event();
        event.setDescription("Convention");
        event.setDate(LocalDate.of(2025, 3, 10));
        event.setTime(LocalTime.of(13, 30));
        event.setLocation("Barra da Tijuca");

        assertNull(event.getTitle());
    }

    @Test
    void testingWithInvalidFields_ShouldFail() {
        Event event = new Event();
        event.setTitle("");
        event.setDescription("");
        event.setDate(null);
        event.setTime(null);
        event.setLocation("");
        event.setMaxTickets(null);
        event.setOrganizerId(null);

        assertTrue(event.getTitle().isEmpty());
        assertTrue(event.getDescription().isEmpty());
        assertNull(event.getDate());
        assertNull(event.getTime());
        assertTrue(event.getLocation().isEmpty());
        assertNull(event.getMaxTickets());
        assertNull(event.getOrganizerId());
    }
    @Test
    void maxTickets_GreaterThanZero() {
        Event event = new Event();
        event.setMaxTickets(-5);
        assertFalse(event.getMaxTickets() > 0, "Max tickets must be greater than zero");
    }

    @Test
    void testingWithValidFields_ShouldPass() {
        Event event = new Event();
        event.setTitle("ADM Lecture");
        event.setDescription("lecture about administration.");
        event.setDate(LocalDate.of(2025, 3, 10));
        event.setTime(LocalTime.of(13, 30));
        event.setLocation("Copacabana");
        event.setMaxTickets(100);
        event.setOrganizerId(1L);

        assertNotNull(event.getTitle());
        assertNotNull(event.getDescription());
        assertNotNull(event.getDate());
        assertNotNull(event.getTime());
        assertNotNull(event.getLocation());
        assertNotNull(event.getMaxTickets());
        assertNotNull(event.getOrganizerId());
    }
    @Test
    void eventSettersAndGetters_ShouldWork() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Tech Conference");
        event.setDescription("a technology conference.");
        event.setDate(LocalDate.of(2025, 3, 14));
        event.setTime(LocalTime.of(10, 30));
        event.setLocation("copacabana");
        event.setMaxTickets(500);
        event.setOrganizerId(2L);

        assertEquals(1L, event.getId());
        assertEquals("Tech Conference", event.getTitle());
        assertEquals("a technology conference.", event.getDescription());
        assertEquals(LocalDate.of(2025, 3, 14), event.getDate());
        assertEquals(LocalTime.of(10, 30), event.getTime());
        assertEquals("copacabana", event.getLocation());
        assertEquals(500, event.getMaxTickets());
        assertEquals(2L, event.getOrganizerId());
    }

}
