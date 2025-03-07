package br.com.orbis.Orbis.model;

import br.com.orbis.Orbis.controller.EventController;
import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.service.EventService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SpeakerTest {

    @Test
    void NameIsBlank_shouldFail() {
        Speaker speaker = new Speaker();
        speaker.setName("");
        speaker.setBio("standard biography");

        assertTrue(speaker.getName().isEmpty(), "Speaker name is required");
    }

    @Test
    void NameIsValid_shouldPass() {
        Speaker speaker = new Speaker();
        speaker.setName("Solange");
        speaker.setBio("standard biography2");

        assertFalse(speaker.getName().isEmpty());
    }

    @Test
    void speakerSettersAndGetters_ShouldWork() {
        Speaker speaker = new Speaker();
        speaker.setId(1L);
        speaker.setName("Alicia");
        speaker.setBio("AWS Specialist");

        assertEquals(1L, speaker.getId());
        assertEquals("Alicia", speaker.getName());
        assertEquals("AWS Specialist", speaker.getBio());
    }
}