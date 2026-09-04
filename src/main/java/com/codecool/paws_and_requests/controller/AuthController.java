package com.codecool.paws_and_requests.controller;

import com.codecool.paws_and_requests.dto.LoginRequest;
import com.codecool.paws_and_requests.dto.RegistrationRequest;
import com.codecool.paws_and_requests.dto.TokenResponse;
import com.codecool.paws_and_requests.exception.ShelterNotFoundException;
import com.codecool.paws_and_requests.exception.UsernameTakenException;
import com.codecool.paws_and_requests.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService ls) {
        this.authService = ls;
    }

    /**
     * Authenticates a user based on the provided credentials.
     *
     * @param request the login request containing the user's credentials
     * @return a {@link TokenResponse} containing the issued access token
     */
    @PostMapping("/login")
    public TokenResponse login(final @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    /**
     * Authenticates a user based on the provided credentials.
     *
     * @param request the login request containing the user's credentials
     * @return a {@link TokenResponse} containing the issued access token
     */
    @PostMapping("/registration")
    public TokenResponse registration(
            final @RequestBody @Valid RegistrationRequest request) {
        return authService.registration(request);
    }

    /**
     * Handles authentication failures caused by invalid credentials.
     *
     * @param ex thrown exception
     * @return String error message
     */
    @ExceptionHandler(BadCredentialsException.class)
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
    public String handleShelterNotFound(final ShelterNotFoundException ex) {
        return ex.getMessage();
    }
}
