package com.service.working.hours.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.service.working.hours.rest.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.service.working.hours.exception.ErrorCodeMapping.AUTHENTICATION_ERROR_CODE;
import static com.service.working.hours.exception.ErrorCodeMapping.DEFAULT_CODE;
import static com.service.working.hours.exception.ErrorCodeMapping.ENTITY_NOT_FOUND_ERROR_CODE;
import static com.service.working.hours.exception.ErrorCodeMapping.GLOBAL_ERROR_CODE;
import static com.service.working.hours.exception.ErrorCodeMapping.PERSISTENCE_ERROR_CODE;
import static com.service.working.hours.exception.ErrorCodeMapping.UNAUTHORIZED_ERROR_CODE;
import static com.service.working.hours.exception.ErrorCodeMapping.VALIDATION_ERROR_CODE;
import static java.lang.String.format;

@Slf4j
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleGlobalError(RuntimeException exception) {
        logError(exception);
        ErrorResponse error = buildErrorResponse(GLOBAL_ERROR_CODE, exception.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ValidationException.class)
    protected ResponseEntity<ErrorResponse> handleValidationError(ValidationException exception) {
        logError(exception);
        ErrorResponse error = buildErrorResponse(VALIDATION_ERROR_CODE, exception.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse> handleDataIntegrityViolationError(DataIntegrityViolationException exception) {
        logError(exception);
        ErrorResponse error = buildErrorResponse(PERSISTENCE_ERROR_CODE, exception.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundError(NotFoundException exception) {
        logError(exception);
        ErrorResponse error = buildErrorResponse(ENTITY_NOT_FOUND_ERROR_CODE, exception.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException exception,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        logError(exception);

        String message = "Invalid input, check your data";
        if (exception.getCause() == null) {
            return new ResponseEntity<>(buildErrorResponse(VALIDATION_ERROR_CODE, message),
                    HttpStatus.BAD_REQUEST);
        }

        if (exception.getCause() instanceof ValueInstantiationException && exception.getCause().getCause() != null) {
            message = "Invalid input: " + exception.getCause().getCause().getMessage();
        } else if (exception.getCause() instanceof UnrecognizedPropertyException) {
            message = format("Unrecognized field '%s'", ((UnrecognizedPropertyException) exception.getCause()).getPropertyName());
        } else if (exception.getCause() instanceof InvalidFormatException) {
            message = "Invalid input: please provide date in correct format YYYY-MM-DD (e.g. 2023-11-28)";
        }

        return new ResponseEntity<>(buildErrorResponse(DEFAULT_CODE, message),
                HttpStatus.BAD_REQUEST);
    }

    private void logError(RuntimeException exception) {
        log.error("Error detected: {} - {}", exception.getClass().getName(), exception.getMessage());
    }

    private ErrorResponse buildErrorResponse(int errorCode, String message) {
        String errorMessage = resolveMessage(errorCode) + ": " + message;

        return new ErrorResponse().code(errorCode).error(errorMessage);
    }

    private String resolveMessage(int errorCode) {
        return switch (errorCode) {
            case GLOBAL_ERROR_CODE -> "Global Error";
            case AUTHENTICATION_ERROR_CODE -> "Invalid credentials";
            case UNAUTHORIZED_ERROR_CODE -> "User is not authorized for requested operation";
            case PERSISTENCE_ERROR_CODE -> "Error detected during persistence";
            case ENTITY_NOT_FOUND_ERROR_CODE -> "NotFoundError: Entity not found";
            case VALIDATION_ERROR_CODE -> "Validation Error";
            default -> "Internal Error";
        };
    }

}
