package com.codecool.paws_and_requests.service;

import com.codecool.paws_and_requests.dto.LoginRequest;
import com.codecool.paws_and_requests.dto.TokenResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.codecool.paws_and_requests.service.JwtService.SECONDS;

@Service
public class LoginService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginService(
            final UserDetailsService udService,
            final PasswordEncoder encoder,
            final JwtService service
    ) {
        this.userDetailsService = udService;
        this.passwordEncoder = encoder;
        this.jwtService = service;
    }

    public final TokenResponse login(final LoginRequest request) {
        try {
            var user = userDetailsService.loadUserByUsername(
                    request.username()
            );

            if (!passwordEncoder.matches(
                    request.password(), user.getPassword())
            ) {
                throw new BadCredentialsException("Invalid credentials");
            }

            return TokenResponse.bearer(
                    jwtService.generateToken(user),
                    jwtService.getExpirationMinutes() * SECONDS
            );
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}
