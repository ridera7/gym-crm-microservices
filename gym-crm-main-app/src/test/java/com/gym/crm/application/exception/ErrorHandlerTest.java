package com.gym.crm.application.exception;

import com.gym.crm.application.rest.dto.Error;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {

    @InjectMocks
    ErrorHandler errorHandler;

    @Test
    void testHandleGlobalError() {
        RuntimeException exception = new RuntimeException("Test runtime exception");
        ResponseEntity<Error> response = errorHandler.handleGlobalError(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Global Error: Test runtime exception", response.getBody().getMessage());
    }

    @Test
    void shouldHandleAuthenticationError() {
        AuthenticationException exception = new AuthenticationException("Invalid credentials");
        ResponseEntity<Error> response = errorHandler.handleAuthenticationError(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials: Invalid credentials", response.getBody().getMessage());
    }

    @Test
    void shouldHandleUnauthorizedError() {
        UnauthorizedException exception = new UnauthorizedException("User not authorized");
        ResponseEntity<Error> response = errorHandler.handleUnauthorizedError(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User is not authorized for requested operation: User not authorized", response.getBody().getMessage());
    }

    @Test
    void shouldHandleValidationError() {
        ValidationException exception = new ValidationException("Validation failed");
        ResponseEntity<Error> response = errorHandler.handleValidationError(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Internal Error: Validation failed", response.getBody().getMessage());
    }

    @Test
    void shouldHandleDataIntegrityViolationError() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Data integrity error");
        ResponseEntity<Error> response = errorHandler.handleDataIntegrityViolationError(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error detected during persistence: Data integrity error", response.getBody().getMessage());
    }

    @Test
    void shouldHandleNotFoundError() {
        NotFoundException exception = new NotFoundException("Entity not found");
        ResponseEntity<Error> response = errorHandler.handleNotFoundError(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("NotFoundError: Entity not found: Entity not found", response.getBody().getMessage());
    }

    @Test
    void shouldHandleServiceUnavailableError() {
        ServiceUnavailableException exception = new ServiceUnavailableException("Service unavailable");

        ResponseEntity<Error> response = errorHandler.handleServiceUnavailableError(exception);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Service unavailable: Service unavailable", response.getBody().getMessage());
    }

}