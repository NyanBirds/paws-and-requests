package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.LoginRequest;
import com.codecool.pawsandrequests.dto.TokenResponse;
import com.codecool.pawsandrequests.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginService loginService;

    public AuthController(final LoginService ls) {
        this.loginService = ls;
    }

    /**
     * Authenticates a user based on the provided credentials.
     *
     * @param request the login request containing the user's credentials
     * @return a {@link TokenResponse} containing the issued access token
     */
    @PostMapping("/login")
    public TokenResponse login(final @RequestBody LoginRequest request) {
        return loginService.login(request);
    }

    /**
     * Handles authentication failures caused by invalid credentials.
     *
     * @return a response with status {@link HttpStatus#UNAUTHORIZED} and
     * no body
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
