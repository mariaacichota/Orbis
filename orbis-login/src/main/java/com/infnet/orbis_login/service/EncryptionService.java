package com.infnet.orbis_login.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final PasswordEncoder passwordEncoder;

    public EncryptionService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean verifyPassword(String password, String encryptedPassword) {
        return passwordEncoder.matches(password, encryptedPassword);
    }

}
