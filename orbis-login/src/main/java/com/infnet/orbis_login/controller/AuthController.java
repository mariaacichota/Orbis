package com.infnet.orbis_login.controller;

import com.infnet.orbis_login.dto.AuthResponse;
import com.infnet.orbis_login.dto.SignInRequest;
import com.infnet.orbis_login.dto.SignUpRequest;
import com.infnet.orbis_login.model.Role;
import com.infnet.orbis_login.model.User;
import com.infnet.orbis_login.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.info("[POST] /auth/sign-up - Criando novo usuário com e-mail: {}", signUpRequest.getEmail());
        try {
            User user = new User();
            user.setName(signUpRequest.getName());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(signUpRequest.getPassword());
            user.setRole(Role.valueOf(signUpRequest.getRole().toUpperCase()));

            User createdUser = userService.createUser(user);

            AuthResponse response = new AuthResponse(
                    createdUser.getId(),
                    createdUser.getName(),
                    createdUser.getRole() != null ? createdUser.getRole().toString() : null
            );

            log.info("Usuário criado com sucesso: {}", createdUser.getEmail());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Erro ao criar usuário: {}", e.getMessage());
            throw new RuntimeException("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        log.info("[POST] /auth/sign-in - Tentando autenticar usuário com e-mail: {}", signInRequest.getEmail());
        try {
            User user = userService.signIn(signInRequest.getEmail(), signInRequest.getPassword());

            AuthResponse response = new AuthResponse(
                    user.getId(),
                    user.getName(),
                    user.getRole() != null ? user.getRole().toString() : null
            );

            log.info("Usuário autenticado com sucesso: {}", user.getEmail());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Erro ao autenticar usuário: {}", e.getMessage());
            throw new RuntimeException("Erro na autenticação: " + e.getMessage());
        }
    }
}
