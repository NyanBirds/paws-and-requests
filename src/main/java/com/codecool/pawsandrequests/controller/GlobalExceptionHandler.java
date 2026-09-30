package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.ErrorResponse;
import com.codecool.pawsandrequests.exception.ResourceNotFoundException;
import com.codecool.pawsandrequests.exception.ShelterNotFoundException;
import com.codecool.pawsandrequests.exception.EmailTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
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
