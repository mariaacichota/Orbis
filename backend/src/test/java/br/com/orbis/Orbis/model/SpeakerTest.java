package br.com.orbis.Orbis.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpeakerTest {

    @Test
    void NameIsBlankShouldFail() {
        Speaker speaker = new Speaker();
        speaker.setName("");
        speaker.setBio("standard biography");

        assertTrue(speaker.getName().isEmpty(), "Speaker name is required");
    }

    @Test
    void NameIsValidShouldPass() {
        Speaker speaker = new Speaker();
        speaker.setName("Solange");
        speaker.setBio("standard biography2");

        assertFalse(speaker.getName().isEmpty());
    }

    @Test
    void speakerSettersAndGettersShouldWork() {
        Speaker speaker = new Speaker();
        speaker.setId(1L);
        speaker.setName("Alicia");
        speaker.setBio("AWS Specialist");

        assertEquals(1L, speaker.getId());
        assertEquals("Alicia", speaker.getName());
        assertEquals("AWS Specialist", speaker.getBio());
    }
}