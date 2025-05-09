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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;



    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String rawPassword = loginData.get("password");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, rawPassword));

            String token = jwtTokenProvider.generateToken(authentication);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok("Bearer " + response);

        } catch (BadCredentialsException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Usuário inexistente ou senha inválida.");
            return ResponseEntity.status(401).body(errorResponse);
        } catch (Exception ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Erro inesperado: " + ex.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody User user) {
        String rawPassword = user.getPassword();

        try{
            User newUser = userService.createUser(user);
        }catch (UserValidationException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", ex.getMessage());
            return ResponseEntity.status(400).body(errorResponse);
        } catch (Exception ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Erro inesperado: " + ex.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), rawPassword));
            String token = jwtTokenProvider.generateToken(authentication);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok("Bearer " + response);
        } catch (BadCredentialsException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Usuário ou senha inválidos.");
            return ResponseEntity.status(401).body(errorResponse);
        } catch (Exception ex) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Erro inesperado: " + ex.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
    }



    }
}
