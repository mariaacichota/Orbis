package br.com.orbis.Orbis.model;

import org.junit.jupiter.api.Test;

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

