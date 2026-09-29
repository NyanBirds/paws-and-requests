package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.LoginRequest;
import com.codecool.pawsandrequests.dto.RegistrationRequest;
import com.codecool.pawsandrequests.dto.TokenResponse;
import com.codecool.pawsandrequests.exception.EmailTakenException;
import com.codecool.pawsandrequests.exception.ShelterNotFoundException;
import com.codecool.pawsandrequests.model.CustomUserDetails;
import com.codecool.pawsandrequests.model.Role;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.model.User;
import com.codecool.pawsandrequests.repository.ShelterRepository;
import com.codecool.pawsandrequests.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.codecool.pawsandrequests.TestFixtures.EMAIL;
import static com.codecool.pawsandrequests.TestFixtures.ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.RAW_PASSWORD;
import static com.codecool.pawsandrequests.TestFixtures.details;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static com.codecool.pawsandrequests.TestFixtures.shelterUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService")
class AuthServiceTest {

    private static final long EXPIRATION_MINUTES = 60L;
    private static final String TOKEN = "header.payload.signature";
    private static final String ENCODED = "{bcrypt}encoded";

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShelterRepository shelterRepository;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userDetailsService, passwordEncoder,
                jwtService, userRepository, shelterRepository);
    }

    @Nested
    @DisplayName("login")
    class Login {

        @Test
        @DisplayName("returns a bearer token for valid credentials")
        void returnsTokenForValidCredentials() {
            CustomUserDetails user = details(shelterUser(shelter()));
            when(userDetailsService.loadUserByUsername(EMAIL))
                    .thenReturn(user);
            when(passwordEncoder.matches(RAW_PASSWORD, user.getPassword()))
                    .thenReturn(true);
            when(jwtService.generateToken(user)).thenReturn(TOKEN);
            when(jwtService.getExpirationMinutes())
                    .thenReturn(EXPIRATION_MINUTES);

            TokenResponse response = authService.login(
                    new LoginRequest(EMAIL, RAW_PASSWORD)
            );

            assertThat(response.token()).isEqualTo(TOKEN);
            assertThat(response.type()).isEqualTo("Bearer");
            assertThat(response.expiresInSeconds())
                    .isEqualTo(EXPIRATION_MINUTES * JwtService.SECONDS);
        }

        @Test
        @DisplayName("normalises the email before looking the user up")
        void normalisesEmail() {
            CustomUserDetails user = details(shelterUser(shelter()));
            when(userDetailsService.loadUserByUsername(EMAIL))
                    .thenReturn(user);
            when(passwordEncoder.matches(RAW_PASSWORD, user.getPassword()))
                    .thenReturn(true);
            when(jwtService.generateToken(user)).thenReturn(TOKEN);

            authService.login(new LoginRequest(
                    "  Ada@Example.COM ", RAW_PASSWORD
            ));

            verify(userDetailsService).loadUserByUsername(EMAIL);
        }

        @Test
        @DisplayName("rejects a wrong password without issuing a token")
        void rejectsWrongPassword() {
            CustomUserDetails user = details(shelterUser(shelter()));
            when(userDetailsService.loadUserByUsername(EMAIL))
                    .thenReturn(user);
            when(passwordEncoder.matches("wrong", user.getPassword()))
                    .thenReturn(false);

            assertThatThrownBy(() -> authService.login(
                    new LoginRequest(EMAIL, "wrong")))
                    .isInstanceOf(BadCredentialsException.class)
                    .hasMessage("Invalid credentials");

            verify(jwtService, never()).generateToken(any());
        }

        @Test
        @DisplayName("hides whether the account exists")
        void hidesUnknownUser() {
            when(userDetailsService.loadUserByUsername(EMAIL))
                    .thenThrow(new UsernameNotFoundException("nope"));

            assertThatThrownBy(() -> authService.login(
                    new LoginRequest(EMAIL, RAW_PASSWORD)))
                    .isInstanceOf(BadCredentialsException.class)
                    .hasMessage("Invalid credentials");

            verifyNoInteractions(jwtService);
        }
    }

    @Nested
    @DisplayName("registration")
    class Registration {

        private RegistrationRequest request(final String shelterOrg) {
            return new RegistrationRequest("Ada", "Lovelace", EMAIL,
                    "070-1234567", RAW_PASSWORD, null, shelterOrg);
        }

        @Test
        @DisplayName("creates a plain USER when no shelter is given")
        void createsPlainUser() {
            when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
            when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED);
            when(jwtService.generateToken(any())).thenReturn(TOKEN);

            authService.registration(request(null));

            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(
                    User.class
            );
            verify(userRepository).save(captor.capture());
            User saved = captor.getValue();

            assertThat(saved.getRole()).isEqualTo(Role.USER);
            assertThat(saved.getShelter()).isNull();
            assertThat(saved.getEmail()).isEqualTo(EMAIL);
            assertThat(saved.getFirstName()).isEqualTo("Ada");
            assertThat(saved.getLastName()).isEqualTo("Lovelace");
            assertThat(saved.getPhoneNumber()).isEqualTo("070-1234567");
            assertThat(saved.getPassword()).isEqualTo(ENCODED);
        }

        @Test
        @DisplayName("creates a SHELTERUSER when a shelter is given")
        void createsShelterUser() {
            Shelter shelter = shelter();
            when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
            when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED);
            when(shelterRepository.findById(ORG_NR))
                    .thenReturn(Optional.of(shelter));
            when(jwtService.generateToken(any())).thenReturn(TOKEN);

            authService.registration(request(ORG_NR));

            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(
                    User.class
            );
            verify(userRepository).save(captor.capture());

            assertThat(captor.getValue().getRole())
                    .isEqualTo(Role.SHELTERUSER);
            assertThat(captor.getValue().getShelter()).isSameAs(shelter);
        }

        @Test
        @DisplayName("treats a blank shelter org as no shelter")
        void blankShelterOrgMeansPlainUser() {
            when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
            when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED);
            when(jwtService.generateToken(any())).thenReturn(TOKEN);

            authService.registration(request("   "));

            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(
                    User.class
            );
            verify(userRepository).save(captor.capture());

            assertThat(captor.getValue().getRole()).isEqualTo(Role.USER);
            verifyNoInteractions(shelterRepository);
        }

        @Test
        @DisplayName("stores the email trimmed and lowercased")
        void normalisesEmail() {
            when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn(ENCODED);
            when(jwtService.generateToken(any())).thenReturn(TOKEN);

            authService.registration(new RegistrationRequest("Ada",
                    "Lovelace", "  Ada@Example.COM ", "070", RAW_PASSWORD,
                    null, null));

            verify(userRepository).existsByEmail(EMAIL);
            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(
                    User.class
            );
            verify(userRepository).save(captor.capture());
            assertThat(captor.getValue().getEmail()).isEqualTo(EMAIL);
        }

        @Test
        @DisplayName("rejects an email that is already taken")
        void rejectsDuplicateEmail() {
            when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

            assertThatThrownBy(() -> authService.registration(
                    request(null)))
                    .isInstanceOf(EmailTakenException.class)
                    .hasMessage("Email already taken: " + EMAIL);

            verify(userRepository, never()).save(any());
            verifyNoInteractions(jwtService, passwordEncoder);
        }

        @Test
        @DisplayName("rejects an unknown shelter")
        void rejectsUnknownShelter() {
            when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
            when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED);
            when(shelterRepository.findById(ORG_NR))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> authService.registration(
                    request(ORG_NR)))
                    .isInstanceOf(ShelterNotFoundException.class)
                    .hasMessage("Shelter not found: " + ORG_NR);

            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("issues a token for the freshly saved user")
        void issuesTokenAfterSaving() {
            CustomUserDetails user = details(shelterUser(shelter()));
            when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
            when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED);
            when(userDetailsService.loadUserByUsername(EMAIL))
                    .thenReturn(user);
            when(jwtService.generateToken(user)).thenReturn(TOKEN);
            when(jwtService.getExpirationMinutes())
                    .thenReturn(EXPIRATION_MINUTES);

            TokenResponse response = authService.registration(
                    request(null)
            );

            assertThat(response.token()).isEqualTo(TOKEN);
            assertThat(response.type()).isEqualTo("Bearer");
            assertThat(response.expiresInSeconds())
                    .isEqualTo(EXPIRATION_MINUTES * JwtService.SECONDS);

            InOrder inOrder = inOrder(
                    userRepository, userDetailsService
            );
            inOrder.verify(userRepository).save(any());
            inOrder.verify(userDetailsService).loadUserByUsername(EMAIL);
        }
    }
}
