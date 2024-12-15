package com.authentication.service.exception;

import com.authentication.service.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {

    @InjectMocks
    ErrorHandler errorHandler;

    @Test
    void testHandleGlobalError() {
        RuntimeException exception = new RuntimeException("Test runtime exception");
        ResponseEntity<ErrorResponse> response = errorHandler.handleGlobalError(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Global Error: Test runtime exception", Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void shouldHandleAuthenticationError() {
        AuthenticationException exception = new AuthenticationException("Invalid credentials");
        ResponseEntity<ErrorResponse> response = errorHandler.handleAuthenticationError(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials: Invalid credentials", Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void shouldHandleUnauthorizedError() {
        UnauthorizedException exception = new UnauthorizedException("User not authorized");
        ResponseEntity<ErrorResponse> response = errorHandler.handleUnauthorizedError(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User is not authorized for requested operation: User not authorized", Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void shouldHandleTokenError() {
        TokenException exception = new TokenException("Token is invalid");
        ResponseEntity<ErrorResponse> response = errorHandler.handleTokenError(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token is invalid: Token is invalid", Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void shouldHandleDataIntegrityViolationError() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Data integrity error");
        ResponseEntity<ErrorResponse> response = errorHandler.handleDataIntegrityViolationError(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error detected during persistence: Data integrity error", Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void shouldHandleNotFoundError() {
        NotFoundException exception = new NotFoundException("Entity not found");
        ResponseEntity<ErrorResponse> response = errorHandler.handleNotFoundError(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("NotFoundError: Entity not found: Entity not found", Objects.requireNonNull(response.getBody()).getError());
    }

}