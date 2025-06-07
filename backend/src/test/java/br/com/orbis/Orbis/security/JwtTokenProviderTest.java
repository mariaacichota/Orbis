package br.com.orbis.Orbis.security;


import io.jsonwebtoken.MalformedJwtException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private static final String SECRET = "ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=";
    private static final int EXPIRATION = 86400000;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", SECRET);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", EXPIRATION);
    }

    @Test
    void generateTokenSuccess() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");

        String token = jwtTokenProvider.generateToken(authentication);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals("test@example.com", jwtTokenProvider.getUsernameFromToken(token));
    }

    @Test
    void validateTokenValidToken_ReturnsTrue() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        String token = jwtTokenProvider.generateToken(authentication);

        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateTokenInvalidToken_ReturnsFalse() {
        assertFalse(jwtTokenProvider.validateToken("invalid.token.here"));
    }

    @Test
    void validateTokenExpiredTokenReturnsFalse() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", -1000); // Token já expirado
        String token = jwtTokenProvider.generateToken(authentication);

        assertFalse(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateTokenNullTokenReturnsFalse() {
        assertFalse(jwtTokenProvider.validateToken(null));
    }

    @Test
    void validateTokenEmptyTokenReturnsFalse() {
        assertFalse(jwtTokenProvider.validateToken(""));
    }

    @Test
    void getUsernameFromTokenSuccess() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        String token = jwtTokenProvider.generateToken(authentication);

        String username = jwtTokenProvider.getUsernameFromToken(token);

        assertEquals("test@example.com", username);
    }

    @Test
    void getUsernameFromTokenInvalidToken_ThrowsException() {
        assertThrows(MalformedJwtException.class, () -> {
            jwtTokenProvider.getUsernameFromToken("invalid.token.here");
        });
    }

    @Test
    void generateTokenWithDifferentUsersProducesDifferentTokens() {
        Authentication auth1 = mock(Authentication.class);
        Authentication auth2 = mock(Authentication.class);
        when(auth1.getName()).thenReturn("user1@example.com");
        when(auth2.getName()).thenReturn("user2@example.com");

        String token1 = jwtTokenProvider.generateToken(auth1);
        String token2 = jwtTokenProvider.generateToken(auth2);

        assertNotEquals(token1, token2);
        assertEquals("user1@example.com", jwtTokenProvider.getUsernameFromToken(token1));
        assertEquals("user2@example.com", jwtTokenProvider.getUsernameFromToken(token2));
    }
}
