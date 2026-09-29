package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.LoginRequest;
import com.codecool.pawsandrequests.dto.RegistrationRequest;
import com.codecool.pawsandrequests.dto.TokenResponse;
import com.codecool.pawsandrequests.dto.UserResponse;
import com.codecool.pawsandrequests.mapper.UserMapper;
import com.codecool.pawsandrequests.model.CustomUserDetails;
import com.codecool.pawsandrequests.model.Role;
import com.codecool.pawsandrequests.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static com.codecool.pawsandrequests.TestFixtures.EMAIL;
import static com.codecool.pawsandrequests.TestFixtures.RAW_PASSWORD;
import static com.codecool.pawsandrequests.TestFixtures.admin;
import static com.codecool.pawsandrequests.TestFixtures.details;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController")
class AuthControllerTest {

    private static final String TOKEN = "header.payload.signature";

    @Mock
    private AuthService authService;

    @Mock
    private UserMapper userMapper;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService, userMapper);
    }

    @Test
    @DisplayName("returns the service token for a login request")
    void returnsTokenForLogin() {
        LoginRequest request = new LoginRequest(EMAIL, RAW_PASSWORD);
        when(authService.login(request)).thenReturn(
                TokenResponse.bearer(TOKEN, 3600L)
        );

        TokenResponse response = authController.login(request);

        assertThat(response.token()).isEqualTo(TOKEN);
        assertThat(response.type()).isEqualTo("Bearer");
        assertThat(response.expiresInSeconds()).isEqualTo(3600L);
    }

    @Test
    @DisplayName("passes a registration request straight through")
    void returnsTokenForRegistration() {
        RegistrationRequest request = new RegistrationRequest("Ada",
                "Lovelace", EMAIL, "070", RAW_PASSWORD, null, null);
        when(authService.registration(request)).thenReturn(
                TokenResponse.bearer(TOKEN, 3600L)
        );

        ResponseEntity<TokenResponse> response = authController.registration(request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assert response.getBody() != null;
        assertThat(response.getBody().token()).isEqualTo(TOKEN);
    }

    @Test
    @DisplayName("returns the mapped principal for an authenticated caller")
    void returnsCurrentUser() {
        CustomUserDetails principal = details(admin());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities()
        );
        when(userMapper.toUserResponse(principal)).thenReturn(
                new UserResponse(principal.getId(), EMAIL, Role.ADMIN, "Ada",
                        "Lovelace", "070", EMAIL, null)
        );

        ResponseEntity<UserResponse> response =
                authController.getCurrentUser(authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().username()).isEqualTo(EMAIL);
        assertThat(response.getBody().role()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("returns 401 when there is no authentication")
    void returnsUnauthorizedWithoutAuthentication() {
        ResponseEntity<UserResponse> response =
                authController.getCurrentUser(null);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNull();
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("returns 401 for an unauthenticated token")
    void returnsUnauthorizedForUnauthenticatedToken() {
        Authentication unauthenticated =
                new UsernamePasswordAuthenticationToken("someone", null);

        ResponseEntity<UserResponse> response =
                authController.getCurrentUser(unauthenticated);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNull();
        verifyNoInteractions(userMapper);
    }
}
