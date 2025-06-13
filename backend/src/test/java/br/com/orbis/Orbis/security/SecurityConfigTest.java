package br.com.orbis.Orbis.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private SecurityConfig securityConfig;
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(UserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        securityConfig = new SecurityConfig(jwtTokenProvider, userDetailsService);
    }

    @Test
    void passwordEncoderShouldReturnBCryptPasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);

        String password = "testPassword";
        String encodedPassword = encoder.encode(password);

        assertTrue(encoder.matches(password, encodedPassword));
        assertFalse(encoder.matches("wrongPassword", encodedPassword));
    }

    @Test
    void authenticationManagerShouldReturnAuthenticationManager() throws Exception {
        AuthenticationConfiguration authConfig = mock(AuthenticationConfiguration.class);
        AuthenticationManager expectedManager = mock(AuthenticationManager.class);
        when(authConfig.getAuthenticationManager()).thenReturn(expectedManager);

        AuthenticationManager result = securityConfig.authenticationManager(authConfig);

        assertNotNull(result);
        assertEquals(expectedManager, result);
    }

    @Test
    void securityFilterChainShouldConfigureHttpSecurity() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class);
        SecurityFilterChain mockChain = mock(SecurityFilterChain.class);

        doReturn(mockChain).when(http).build();
        doReturn(http).when(http).addFilterBefore(any(), any());

        doAnswer(invocation -> http).when(http).cors(any(Customizer.class));
        doAnswer(invocation -> http).when(http).csrf(any(Customizer.class));
        doAnswer(invocation -> http).when(http).sessionManagement(any(Customizer.class));
        doAnswer(invocation -> http).when(http).authorizeHttpRequests(any(Customizer.class));
        doAnswer(invocation -> http).when(http).exceptionHandling(any(Customizer.class));
        doAnswer(invocation -> http).when(http).oauth2Login(any(Customizer.class));

        SecurityFilterChain result = securityConfig.securityFilterChain(http);

        assertNotNull(result);
        assertEquals(mockChain, result);

        verify(http).cors(any(Customizer.class));
        verify(http).csrf(any(Customizer.class));
        verify(http).sessionManagement(any(Customizer.class));
        verify(http).authorizeHttpRequests(any(Customizer.class));
        verify(http).exceptionHandling(any(Customizer.class));
        verify(http).oauth2Login(any(Customizer.class));
        verify(http).addFilterBefore(any(), any());
    }
}
