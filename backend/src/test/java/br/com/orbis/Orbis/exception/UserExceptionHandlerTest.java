package br.com.orbis.Orbis.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserExceptionHandlerTest {

    private final UserExceptionHandler exceptionHandler = new UserExceptionHandler();

    @Test
    void handleValidationExceptionsShouldReturnMapWithErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        ObjectError fieldError = new FieldError("objectName", "email", "Email inválido");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).containsKey("email"));
        assertEquals("Email inválido", response.getBody().get("email"));
    }

    @Test
    void handleValidationExceptionsWithMultipleErrorsShouldReturnAllErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        List<ObjectError> fieldErrors = List.of(
            new FieldError("objectName", "email", "Email inválido"),
            new FieldError("objectName", "senha", "Senha deve ter no mínimo 6 caracteres")
        );

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(fieldErrors);

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals("Email inválido", response.getBody().get("email"));
        assertEquals("Senha deve ter no mínimo 6 caracteres", response.getBody().get("senha"));
    }
}
