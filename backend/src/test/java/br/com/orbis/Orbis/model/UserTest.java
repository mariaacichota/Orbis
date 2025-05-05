package br.com.orbis.Orbis.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUser() {
        User user = new User();
        user.setName("User Tester");
        user.setEmail("tester@email.com");
        user.setPassword("strongPassword*123");
        user.setRole(Role.PARTICIPANTE);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testBlankUserName() {
        User user = new User();
        user.setName("");
        user.setEmail("tester@email.com");
        user.setPassword("strongPassword*123");
        user.setRole(Role.PARTICIPANTE);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Name cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankEmail() {
        User user = new User();
        user.setName("User Tester");
        user.setEmail("");
        user.setPassword("strongPassword*123");
        user.setRole(Role.PARTICIPANTE);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Email cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidEmail() {
        User user = new User();
        user.setName("User Tester");
        user.setEmail("invalid-e-mail");
        user.setPassword("strongPassword*123");
        user.setRole(Role.PARTICIPANTE);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Email must be valid", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankPassword() {
        User user = new User();
        user.setName("User Tester");
        user.setEmail("tester@email.com");
        user.setPassword("");
        user.setRole(Role.PARTICIPANTE);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Password cannot be blank", violations.iterator().next().getMessage());
    }
}