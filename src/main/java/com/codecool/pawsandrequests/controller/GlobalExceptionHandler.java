package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.exception.ShelterNotFoundException;
import com.codecool.pawsandrequests.exception.EmailTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// REVIEW(api): every handler returns a bare string, so clients get text/plain for errors and JSON for successes and cannot parse either uniformly. Return a ProblemDetail (built into Spring 6) or a small ErrorResponse record so the shape is consistent.
// REVIEW(noob): no handler for MethodArgumentNotValidException, so once you add validation constraints the 400 body will be Spring's default stack-trace-ish blob. No fallback handler for Exception either, which means unexpected failures leak whatever the container prints.
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
}
