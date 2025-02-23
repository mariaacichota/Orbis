package br.com.orbis.Orbis.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean verifyPassword(String password, String encryptedPassword) {
        return passwordEncoder.matches(password, encryptedPassword);
    }

}
