package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.exception.ShelterNotFoundException;
import com.codecool.pawsandrequests.exception.UsernameTakenException;
import org.springframework.http.HttpStatus;
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
    @ExceptionHandler(UsernameTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUsernameTaken(final UsernameTakenException ex) {
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
