package com.codecool.paws_and_requests.service;

import com.codecool.paws_and_requests.dto.LoginRequest;
import com.codecool.paws_and_requests.dto.RegistrationRequest;
import com.codecool.paws_and_requests.dto.TokenResponse;
import com.codecool.paws_and_requests.exception.ShelterNotFoundException;
import com.codecool.paws_and_requests.exception.UsernameTakenException;
import com.codecool.paws_and_requests.model.Role;
import com.codecool.paws_and_requests.model.Shelter;
import com.codecool.paws_and_requests.model.User;
import com.codecool.paws_and_requests.repository.ShelterRepository;
import com.codecool.paws_and_requests.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.codecool.paws_and_requests.service.JwtService.SECONDS;

@Service
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;

    public AuthService(
            final UserDetailsService udService,
            final PasswordEncoder encoder,
            final JwtService service,
            final UserRepository userRepo,
            final ShelterRepository shelterRepo
    ) {
        this.userDetailsService = udService;
        this.passwordEncoder = encoder;
        this.jwtService = service;
        this.userRepository = userRepo;
        this.shelterRepository = shelterRepo;
    }

    public final TokenResponse login(final LoginRequest request) {
        try {
            var user = userDetailsService.loadUserByUsername(
                    request.email()
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

    public final TokenResponse registration(final RegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UsernameTakenException(request.email());
        } else {
            User user = new User();
            user.setFirstName(request.firstname());
            user.setLastName(request.lastname());
            user.setEmail(request.email());
            user.setPhoneNumber(request.phonenumber());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setRole(Role.USER);
            user.setProfilePicture(request.profilePicture());
            String org = request.shelterOrg();
            if (org != null && !org.isBlank()) {
                Shelter shelter = shelterRepository
                        .findById(request.shelterOrg()).orElseThrow(
                                () -> new ShelterNotFoundException(org)
                        );
                user.setShelter(shelter);
            }
            userRepository.save(user);

            var uDetails = userDetailsService.loadUserByUsername(
                    request.email()
            );

            return TokenResponse.bearer(
                    jwtService.generateToken(uDetails),
                    jwtService.getExpirationMinutes() * SECONDS
            );
        }
    }
}
