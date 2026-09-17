package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.LoginRequest;
import com.codecool.pawsandrequests.dto.RegistrationRequest;
import com.codecool.pawsandrequests.dto.TokenResponse;
import com.codecool.pawsandrequests.exception.ShelterNotFoundException;
import com.codecool.pawsandrequests.exception.EmailTakenException;
import com.codecool.pawsandrequests.model.Role;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.model.User;
import com.codecool.pawsandrequests.repository.ShelterRepository;
import com.codecool.pawsandrequests.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.codecool.pawsandrequests.service.JwtService.SECONDS;

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

    // REVIEW(noob): AuthenticationManager with a UsernamePasswordAuthenticationToken is the idiomatic Spring way to do this. Loading the UserDetails and calling passwordEncoder.matches() by hand works, but it bypasses the account-locked / disabled checks that UserDetails already models.
    // REVIEW(good): returning the same 'Invalid credentials' message for an unknown user and a wrong password is exactly right. It stops the endpoint being used to enumerate which emails are registered.
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
            throw new EmailTakenException(request.email());
        } else {
            User user = new User();
            user.setFirstName(request.firstname());
            user.setLastName(request.lastname());
            user.setEmail(request.email());
            user.setPhoneNumber(request.phonenumber());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setRole(Role.USER);
            user.setProfilePicture(request.profilePicture());
            // REVIEW(sec): shelterOrg comes straight from the registration body, so anyone who knows or guesses an org number can attach their new account to that shelter. This is the classic 'the client sent an id it has no business choosing' problem. Shelter membership is something the shelter grants, not something the registrant claims: either have an existing shelter user invite them, or write it to a pending state an admin approves.
            // REVIEW(sec): the role is hardcoded to USER two lines up, which is what currently stops this being exploitable. That is one line away from a privilege escalation, and it also means a shelter employee registering here can never actually act for their shelter.
            String org = request.shelterOrg();
            if (org != null && !org.isBlank()) {
                Shelter shelter = shelterRepository
                        .findById(request.shelterOrg()).orElseThrow(
                                () -> new ShelterNotFoundException(org)
                        );
                user.setRole(Role.SHELTERUSER);
                user.setShelter(shelter);
            }
            userRepository.save(user);
// REVIEW(efficiency): you just saved the user, then load it again through the UserDetailsService to mint the token. new CustomUserDetails(user) is right there.

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
