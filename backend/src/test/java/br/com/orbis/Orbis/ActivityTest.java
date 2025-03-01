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


class ActivityTest {

    @Test
    void validActivity_ShouldPass() {
        Activity activity = new Activity();
        activity.setName("Java lecture");
        activity.setDescription("a lecture about java");

        assertNotNull(activity.getName());
        assertNotNull(activity.getDescription());
    }

    @Test
    void invalidActivity_ShouldFail() {
        Activity activity = new Activity();
        activity.setName(null);
        activity.setDescription(null);

        assertNull(activity.getName());
        assertNull(activity.getDescription());
    }
    @Test
    void activitySettersAndGetters_ShouldWork() {
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setName("java workshop");
        activity.setDescription("workshop about java");

        assertEquals(1L, activity.getId());
        assertEquals("java workshop", activity.getName());
        assertEquals("workshop about java", activity.getDescription());
    }
}

