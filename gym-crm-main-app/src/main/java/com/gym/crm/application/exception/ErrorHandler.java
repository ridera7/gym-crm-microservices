package com.gym.crm.application.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.gym.crm.application.rest.dto.Error;
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

import static com.gym.crm.application.exception.ErrorCodeMapping.AUTHENTICATION_ERROR_CODE;
import static com.gym.crm.application.exception.ErrorCodeMapping.DEFAULT_CODE;
import static com.gym.crm.application.exception.ErrorCodeMapping.ENTITY_NOT_FOUND_ERROR_CODE;
import static com.gym.crm.application.exception.ErrorCodeMapping.GLOBAL_ERROR_CODE;
import static com.gym.crm.application.exception.ErrorCodeMapping.PERSISTENCE_ERROR_CODE;
import static com.gym.crm.application.exception.ErrorCodeMapping.UNAUTHORIZED_ERROR_CODE;
import static com.gym.crm.application.exception.ErrorCodeMapping.VALIDATION_ERROR_CODE;
import static java.lang.String.format;

@Slf4j
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<Error> handleGlobalError(RuntimeException exception) {
        logError(exception);
        Error error = buildErrorResponse(GLOBAL_ERROR_CODE, exception.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    protected ResponseEntity<Error> handleAuthenticationError(AuthenticationException exception) {
        logError(exception);
        Error error = buildErrorResponse(AUTHENTICATION_ERROR_CODE, exception.getMessage());

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    protected ResponseEntity<Error> handleUnauthorizedError(UnauthorizedException exception) {
        logError(exception);
        Error error = buildErrorResponse(UNAUTHORIZED_ERROR_CODE, exception.getMessage());

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = ValidationException.class)
    protected ResponseEntity<Error> handleValidationError(ValidationException exception) {
        logError(exception);
        Error error = buildErrorResponse(VALIDATION_ERROR_CODE, exception.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    protected ResponseEntity<Error> handleDataIntegrityViolationError(DataIntegrityViolationException exception) {
        logError(exception);
        Error error = buildErrorResponse(PERSISTENCE_ERROR_CODE, exception.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<Error> handleNotFoundError(NotFoundException exception) {
        logError(exception);
        Error error = buildErrorResponse(ENTITY_NOT_FOUND_ERROR_CODE, exception.getMessage());

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

    private Error buildErrorResponse(int errorCode, String message) {
        String errorMessage = resolveMessage(errorCode) + ": " + message;

        return new Error(errorCode, errorMessage);
    }

    private String resolveMessage(int errorCode) {
        return switch (errorCode) {
            case GLOBAL_ERROR_CODE -> "Global Error";
            case AUTHENTICATION_ERROR_CODE -> "Invalid credentials";
            case UNAUTHORIZED_ERROR_CODE -> "User is not authorized for requested operation";
            case PERSISTENCE_ERROR_CODE -> "Error detected during persistence";
            case ENTITY_NOT_FOUND_ERROR_CODE -> "NotFoundError: Entity not found";
            default -> "Internal Error";
        };
    }

}
