package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.security.JwtTokenProvider;
import br.com.orbis.Orbis.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserService userService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationManager, jwtTokenProvider, userService, null);
    }

    @Test
    void testSignInSuccess() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", "user@example.com");
        loginData.put("password", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(jwtTokenProvider.generateToken(any())).thenReturn("Bearer dummyToken");

        ResponseEntity<?> response = authController.signIn(loginData);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Bearer dummyToken"));
    }

    @Test
    void testSignInFailureInvalidCredentials() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", "user@example.com");
        loginData.put("password", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Usuário inexistente ou senha inválida."));

        ResponseEntity<?> response = authController.signIn(loginData);

        assertNotNull(response);
        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("message"));
    }

    @Test
    void testSignUpSuccess() {
        Map<String, String> signUpData = new HashMap<>();
        signUpData.put("email", "newuser@example.com");
        signUpData.put("password", "password");

        when(userService.createUser(any())).thenReturn(null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(jwtTokenProvider.generateToken(any())).thenReturn("Bearer dummyToken");

        ResponseEntity<?> response = authController.signUp(new User());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Bearer dummyToken"));
    }

    @Test
    void testSignUpFailure() {
        when(userService.createUser(any())).thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<?> response = authController.signUp(new User());

        assertEquals(500, response.getStatusCodeValue());

        Map<String, String> body = (Map<String, String>) response.getBody();
        assertNotNull(body);
        assertEquals("Erro inesperado: Erro inesperado", body.get("message"));
    }
}