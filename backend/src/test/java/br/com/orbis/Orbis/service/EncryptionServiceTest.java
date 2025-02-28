package br.com.orbis.Orbis.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {

    @Test
    void testEncryptPassword() {
        EncryptionService encryptionService = new EncryptionService();
        String rawPassword = "myPassword123";
        String encryptedPassword = encryptionService.encryptPassword(rawPassword);

        assertNotNull(encryptedPassword);
        assertNotEquals(rawPassword, encryptedPassword);
        assertTrue(encryptionService.verifyPassword(rawPassword, encryptedPassword));
    }

    @Test
    void testVerifyPassword() {
        EncryptionService encryptionService = new EncryptionService();
        String rawPassword = "myPassword123";
        String encryptedPassword = encryptionService.encryptPassword(rawPassword);

        assertTrue(encryptionService.verifyPassword(rawPassword, encryptedPassword));
        assertFalse(encryptionService.verifyPassword("wrongPassword", encryptedPassword));
    }
}