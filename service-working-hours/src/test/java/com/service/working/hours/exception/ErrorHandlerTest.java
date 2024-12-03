package com.service.working.hours.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.service.working.hours.rest.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    ErrorHandler errorHandler;

    @Test
    void shouldReturnInternalServerError() {
        RuntimeException exception = new RuntimeException("Test global error");

        var response = errorHandler.handleGlobalError(exception);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(ErrorCodeMapping.GLOBAL_ERROR_CODE, response.getBody().getCode());
        assertTrue(response.getBody().getError().contains("Global Error: Test global error"));
    }

    @Test
    void shouldReturnBadRequest() {
        ValidationException exception = new ValidationException("Test validation error");

        var response = errorHandler.handleValidationError(exception);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorCodeMapping.VALIDATION_ERROR_CODE, response.getBody().getCode());
        assertTrue(response.getBody().getError().contains("Test validation error"));
    }

    @Test
    void shouldReturnInternalServerError_WhenDataIntegrityViolationError() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Test persistence error");

        var response = errorHandler.handleDataIntegrityViolationError(exception);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(ErrorCodeMapping.PERSISTENCE_ERROR_CODE, response.getBody().getCode());
        assertTrue(response.getBody().getError().contains("Error detected during persistence: Test persistence error"));
    }

    @Test
    void shouldReturnNotFound() {
        NotFoundException exception = new NotFoundException("Test not found error");

        var response = errorHandler.handleNotFoundError(exception);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ErrorCodeMapping.ENTITY_NOT_FOUND_ERROR_CODE, response.getBody().getCode());
        assertTrue(response.getBody().getError().contains("NotFoundError: Entity not found: Test not found error"));
    }

    @Test
    void shouldHandleInvalidFormatException() {
        InvalidFormatException cause = mock(InvalidFormatException.class);
        HttpInputMessage httpInputMessage = mock(HttpInputMessage.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Invalid format", cause, httpInputMessage);

        var response = errorHandler.handleHttpMessageNotReadable(exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorCodeMapping.DEFAULT_CODE, ((ErrorResponse) response.getBody()).getCode());
        assertTrue(((ErrorResponse) response.getBody()).getError().contains("Invalid input: please provide date in correct format YYYY-MM-DD"));
    }

    @Test
    void shouldHandleUnrecognizedPropertyException() {
        UnrecognizedPropertyException cause = mock(UnrecognizedPropertyException.class);
        when(cause.getPropertyName()).thenReturn("unknownField");
        HttpInputMessage httpInputMessage = mock(HttpInputMessage.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Unrecognized field", cause, httpInputMessage);

        var response = errorHandler.handleHttpMessageNotReadable(exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorCodeMapping.DEFAULT_CODE, ((ErrorResponse) response.getBody()).getCode());
        assertTrue(((ErrorResponse) response.getBody()).getError().contains("Unrecognized field 'unknownField'"));
    }

    @Test
    void shouldHandleGenericCase() {
        HttpInputMessage httpInputMessage = mock(HttpInputMessage.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Generic read error", null, httpInputMessage);

        var response = errorHandler.handleHttpMessageNotReadable(exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorCodeMapping.VALIDATION_ERROR_CODE, ((ErrorResponse) response.getBody()).getCode());
        assertTrue(((ErrorResponse) response.getBody()).getError().contains("Invalid input, check your data"));
    }

}