package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.LoginRequest;
import com.codecool.pawsandrequests.dto.RegistrationRequest;
import com.codecool.pawsandrequests.dto.TokenResponse;
import com.codecool.pawsandrequests.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    // REVIEW(noob): @Valid is missing here but present on registration. Right now it makes no difference because LoginRequest has no constraints, which is itself the bug: a null email reaches the database layer before anything complains.
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
    // REVIEW(noob): @Valid is applied but RegistrationRequest declares no constraints at all, so it validates nothing. Put @NotBlank / @Email / @Size on the record components and this starts earning its keep.
    @PostMapping("/registration")
    public TokenResponse registration(
            final @RequestBody @Valid RegistrationRequest request) {
        return authService.registration(request);
    }
}
