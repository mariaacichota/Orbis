package br.com.orbis.Orbis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {

    private BCryptPasswordEncoder passwordEncoder;
    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        encryptionService = new EncryptionService(passwordEncoder);
    }

    @Test
    void testEncryptPassword() {
        String rawPassword = "myPassword123";

        String encryptedPassword = encryptionService.encryptPassword(rawPassword);

        assertNotNull(encryptedPassword);
        assertNotEquals(rawPassword, encryptedPassword);

        assertTrue(encryptionService.verifyPassword(rawPassword, encryptedPassword));
    }

    @Test
    void testVerifyPassword() {
        String rawPassword = "myPassword123";

        String encryptedPassword = encryptionService.encryptPassword(rawPassword);

        assertTrue(encryptionService.verifyPassword(rawPassword, encryptedPassword));

        assertFalse(encryptionService.verifyPassword("wrongPassword", encryptedPassword));
    }
}