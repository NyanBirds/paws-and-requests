package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.config.JwtProperties;
import com.codecool.pawsandrequests.model.User;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static com.codecool.pawsandrequests.TestFixtures.JWT_SECRET;
import static com.codecool.pawsandrequests.TestFixtures.OTHER_JWT_SECRET;
import static com.codecool.pawsandrequests.TestFixtures.details;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static com.codecool.pawsandrequests.TestFixtures.shelterUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtService")
class JwtServiceTest {

    private static final long EXPIRATION_MINUTES = 60L;

    private final JwtService jwtService =
            new JwtService(new JwtProperties(JWT_SECRET, EXPIRATION_MINUTES));

    private final JwtService otherKeyService = new JwtService(
            new JwtProperties(OTHER_JWT_SECRET, EXPIRATION_MINUTES)
    );

    private final UserDetails userDetails =
            details(shelterUser(shelter()));

    @Nested
    @DisplayName("generateToken")
    class GenerateToken {

        @Test
        @DisplayName("produces a compact three part JWS")
        void producesCompactToken() {
            String token = jwtService.generateToken(userDetails);

            assertThat(token).isNotBlank();
            assertThat(token.split("\\.")).hasSize(3);
        }

        @Test
        @DisplayName("embeds the username as the subject")
        void embedsUsername() {
            String token = jwtService.generateToken(userDetails);

            assertThat(jwtService.extractUsername(token))
                    .isEqualTo(userDetails.getUsername());
        }
    }

    @Nested
    @DisplayName("extractUsername")
    class ExtractUsername {

        @Test
        @DisplayName("rejects a token signed with a different key")
        void rejectsForeignSignature() {
            String foreignToken = otherKeyService.generateToken(userDetails);

            assertThatThrownBy(() -> jwtService.extractUsername(foreignToken))
                    .isInstanceOf(JwtException.class);
        }

        @Test
        @DisplayName("rejects a token whose payload was tampered with")
        void rejectsTamperedPayload() {
            String token = jwtService.generateToken(userDetails);
            String[] parts = token.split("\\.");
            String tampered = parts[0] + "." + parts[1].substring(0,
                    parts[1].length() - 1) + "A." + parts[2];

            assertThatThrownBy(() -> jwtService.extractUsername(tampered))
                    .isInstanceOf(JwtException.class);
        }

        @Test
        @DisplayName("rejects a malformed token")
        void rejectsMalformedToken() {
            assertThatThrownBy(() -> jwtService.extractUsername("not-a-jwt"))
                    .isInstanceOf(JwtException.class);
        }
    }

    @Nested
    @DisplayName("isValid")
    class IsValid {

        @Test
        @DisplayName("accepts a fresh token that belongs to the user")
        void acceptsFreshMatchingToken() {
            String token = jwtService.generateToken(userDetails);

            assertThat(jwtService.isValid(token, userDetails)).isTrue();
        }

        @Test
        @DisplayName("rejects a token issued for a different user")
        void rejectsDifferentSubject() {
            String token = jwtService.generateToken(userDetails);
            User other = shelterUser(shelter());
            other.setEmail("grace@example.com");

            assertThat(jwtService.isValid(token, details(other))).isFalse();
        }

        @Test
        @DisplayName("rejects a token signed with a different key")
        void rejectsForeignSignature() {
            String foreignToken = otherKeyService.generateToken(userDetails);

            assertThat(jwtService.isValid(foreignToken, userDetails)).isFalse();
        }

        @Test
        @DisplayName("rejects garbage instead of propagating the error")
        void rejectsGarbage() {
            assertThat(jwtService.isValid("garbage", userDetails)).isFalse();
        }

        @Test
        @DisplayName("rejects an already expired token")
        void rejectsExpiredToken() {
            JwtService expired = new JwtService(
                    new JwtProperties(JWT_SECRET, 0L)
            );
            String token = expired.generateToken(userDetails);

            assertThat(expired.isValid(token, userDetails)).isFalse();
        }
    }

    @Test
    @DisplayName("getExpirationMinutes returns the configured value")
    void exposesExpirationMinutes() {
        assertThat(jwtService.getExpirationMinutes())
                .isEqualTo(EXPIRATION_MINUTES);
    }

    @Test
    @DisplayName("SECONDS is the seconds-per-minute conversion factor")
    void secondsConstant() {
        assertThat(JwtService.SECONDS).isEqualTo(60);
    }

    @Test
    @DisplayName("different users get different subjects")
    void subjectsArePerUser() {
        User other = shelterUser(shelter());
        other.setEmail("grace@example.com");

        String token = jwtService.generateToken(details(other));

        assertThat(jwtService.extractUsername(token))
                .isEqualTo("grace@example.com");
    }
}
