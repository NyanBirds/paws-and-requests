package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.ErrorResponse;
import com.codecool.pawsandrequests.dto.ValidationErrorResponse;
import com.codecool.pawsandrequests.exception.ResourceNotFoundException;
import com.codecool.pawsandrequests.exception.ShelterNotFoundException;
import com.codecool.pawsandrequests.exception.EmailTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.TreeMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Code returned with every validation failure, so a client can branch on
     * it without matching on message text.
     */
    public static final String VALIDATION_FAILED = "VALIDATION_FAILED";

    /**
     * Handles a request body that failed bean validation.
     *
     * <p>One entry per offending field, which lets a form mark the individual
     * inputs rather than showing a single opaque message. A field can be
     * reported by more than one constraint (a blank password trips both
     * {@code @NotBlank} and {@code @Size}), so messages for the same field are
     * concatenated rather than overwriting each other.
     *
     * @param exception thrown exception carrying the field errors
     * @return a 400 with every offending field and its message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(
            final MethodArgumentNotValidException exception
    ) {
        // Sorted so the body is stable across requests: Spring does not
        // promise an order for getFieldErrors(), and an unstable body would
        // make both the tests and the rendered form flicker.
        final Map<String, String> fields = new TreeMap<>();

        for (final FieldError error : exception.getBindingResult()
                .getFieldErrors()) {
            fields.merge(error.getField(), error.getDefaultMessage(),
                    (first, next) -> first + " " + next);
        }

        final ValidationErrorResponse body = new ValidationErrorResponse(
                VALIDATION_FAILED,
                "Validation failed",
                fields
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Handles authentication failures caused by invalid credentials.
     *
     * @param ex thrown exception
     * @return String error message
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleBadCredentials(final BadCredentialsException ex) {
        return ex.getMessage();
    }

    /**
     * Handles registration failures  caused by username already taken
     *
     * @param ex thrown exception
     * @return String error message
     */
    @ExceptionHandler(EmailTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUsernameTaken(final EmailTakenException ex) {
        return ex.getMessage();
    }

    /**
     * Handles shelter not found
     *
     * @param ex thrown exception
     * @return String error message
     */
    @ExceptionHandler(ShelterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleShelterNotFound(final ShelterNotFoundException ex) {
        return ex.getMessage();
    }

    /**
     * Handles generic resource not found
     *
     * @param exception thrown exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            final ResourceNotFoundException exception
    ) {
        ErrorResponse error = new ErrorResponse(
                "RESOURCE_NOT_FOUND",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}
