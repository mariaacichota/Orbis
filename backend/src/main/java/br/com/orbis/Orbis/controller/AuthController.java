package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.exception.UserValidationException;
import br.com.orbis.Orbis.security.JwtTokenProvider;
import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String MESSAGE = "message";

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, String>> signIn(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String rawPassword = loginData.get("password");
        log.info("[POST] /api/auth/sign-in - Tentativa de login para o e-mail: {}", email);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, rawPassword));
            Map<String, String> response = new HashMap<>();
            response.put("token", "Bearer " + jwtTokenProvider.generateToken(authentication));
            log.info("Login realizado com sucesso para o e-mail: {}", email);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            log.warn("Credenciais inválidas para o e-mail: {}", email);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(MESSAGE, "Usuário inexistente ou senha inválida.");
            return ResponseEntity.status(401).body(errorResponse);
        } catch (Exception e) {
            log.error("Erro ao realizar login para o e-mail: {}", email, e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(MESSAGE, "Erro inesperado: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> signUp(@Valid @RequestBody User user) {
        String rawPassword = user.getPassword();

        try {
            var created = userService.createUser(user);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), rawPassword));
            Map<String, String> response = new HashMap<>();
            response.put("name", created.getName());
            response.put("id", created.getId().toString());
            response.put("role", created.getRole().toString());
            response.put("token", "Bearer " + jwtTokenProvider.generateToken(authentication));
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(MESSAGE, "Usuário ou senha inválidos.");
            return ResponseEntity.status(401).body(errorResponse);
        } catch (UserValidationException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(MESSAGE, ex.getMessage());
            return ResponseEntity.status(400).body(errorResponse);
        } catch (Exception ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(MESSAGE, "Erro inesperado: " + ex.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
