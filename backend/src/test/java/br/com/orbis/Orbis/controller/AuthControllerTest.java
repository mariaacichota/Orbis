package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.exception.UserValidationException;
import br.com.orbis.Orbis.model.Role;
import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.security.JwtTokenProvider;
import br.com.orbis.Orbis.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        authController = new AuthController(authenticationManager, jwtTokenProvider, userService);
    }

    @Test
    void testSignInSuccess() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", "newuser@example.com");
        loginData.put("password", "password123");

        User mockUser = new User();
        mockUser.setEmail("newuser@example.com");
        mockUser.setPassword("password123");
        mockUser.setName("Test User");
        mockUser.setId(1L);
        mockUser.setRole(Role.ORGANIZADOR);

        Authentication mockAuth = new UsernamePasswordAuthenticationToken(mockUser.getEmail(), mockUser.getPassword());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);

        when(userService.getUserByEmail("newuser@example.com")).thenReturn(mockUser);

        when(jwtTokenProvider.generateToken(any())).thenReturn("dummyToken");

        ResponseEntity<?> response = authController.signIn(loginData);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Bearer dummyToken", responseBody.get("token"));
        assertEquals("Test User", responseBody.get("name"));
        assertEquals("1", responseBody.get("id"));
        assertEquals("ORGANIZADOR", responseBody.get("role"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).getUserByEmail("newuser@example.com");
        verify(jwtTokenProvider).generateToken(any());
    }

    @Test
    void testSignUpSuccess() {
        User newUser = new User();
        newUser.setId(1L);
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password123");
        newUser.setName("Test User");
        newUser.setId(1L);
        newUser.setRole(Role.ORGANIZADOR);

        when(userService.createUser(any(User.class))).thenReturn(newUser);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(newUser.getEmail(), newUser.getPassword()));

        when(jwtTokenProvider.generateToken(any())).thenReturn("dummyToken");

        ResponseEntity<?> response = authController.signUp(newUser);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Bearer dummyToken", responseBody.get("token"));
        assertEquals("Test User", responseBody.get("name"));
        assertEquals("1", responseBody.get("id"));
        assertEquals("ORGANIZADOR", responseBody.get("role"));

        verify(userService).createUser(any(User.class));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(any());
    }

    @Test
    void testSignUpFailure() {
        when(userService.createUser(any())).thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<?> response = authController.signUp(new User());

        Map<String, String> body = (Map<String, String>) response.getBody();
        assertNotNull(body);
        assertEquals("Erro inesperado: Erro inesperado", body.get("message"));
    }

    @Test
    void testSignInWithBadCredentials() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", "user@example.com");
        loginData.put("password", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        ResponseEntity<?> response = authController.signIn(loginData);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Usuário inexistente ou senha inválida.", responseBody.get("message"));
    }

    @Test
    void testSignInWithUnexpectedException() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", "user@example.com");
        loginData.put("password", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<?> response = authController.signIn(loginData);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Erro inesperado: Erro inesperado", responseBody.get("message"));
    }

    @Test
    void testSignUpWithBadCredentials() {
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password");

        when(userService.createUser(any())).thenReturn(newUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        ResponseEntity<?> response = authController.signUp(newUser);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Usuário ou senha inválidos.", responseBody.get("message"));
    }

    @Test
    void testSignUpWithUserValidationException() {
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password");

        when(userService.createUser(any())).thenReturn(newUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UserValidationException("Erro de validação do usuário"));

        ResponseEntity<?> response = authController.signUp(newUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Erro de validação do usuário", responseBody.get("message"));
    }
}