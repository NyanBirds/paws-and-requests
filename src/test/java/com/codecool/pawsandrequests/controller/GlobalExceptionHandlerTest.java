package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.exception.EmailTakenException;
import com.codecool.pawsandrequests.exception.ShelterNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    private static HttpStatus statusOf(final String methodName,
                                       final Class<?> exceptionType)
            throws NoSuchMethodException {
        Method method = GlobalExceptionHandler.class.getMethod(methodName,
                exceptionType);
        ResponseStatus annotation =
                method.getAnnotation(ResponseStatus.class);
        return annotation == null ? null : annotation.value();
    }

    @Test
    @DisplayName("maps bad credentials to 401 with the original message")
    void mapsBadCredentials() throws NoSuchMethodException {
        BadCredentialsException ex = new BadCredentialsException(
                "Invalid credentials"
        );

        assertThat(handler.handleBadCredentials(ex))
                .isEqualTo("Invalid credentials");
        assertThat(statusOf("handleBadCredentials",
                BadCredentialsException.class))
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("maps a taken email to 409 with the original message")
    void mapsEmailTaken() throws NoSuchMethodException {
        EmailTakenException ex = new EmailTakenException("ada@example.com");

        assertThat(handler.handleUsernameTaken(ex))
                .isEqualTo("Email already taken: ada@example.com");
        assertThat(statusOf("handleUsernameTaken",
                EmailTakenException.class)).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("maps an unknown shelter to 404 with the original message")
    void mapsShelterNotFound() throws NoSuchMethodException {
        ShelterNotFoundException ex = new ShelterNotFoundException("556677");

        assertThat(handler.handleShelterNotFound(ex))
                .isEqualTo("Shelter not found: 556677");
        assertThat(statusOf("handleShelterNotFound",
                ShelterNotFoundException.class))
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("does not leak exception type names to the client")
    void hidesExceptionTypes() {
        assertThat(handler.handleBadCredentials(
                new BadCredentialsException("Invalid credentials")))
                .doesNotContain("Exception")
                .doesNotContain("com.codecool");
    }

    @Test
    @DisplayName("returns the message unchanged, never null")
    void returnsMessageUnchanged() {
        assertThat(handler.handleBadCredentials(
                new BadCredentialsException("custom message")))
                .isEqualTo("custom message");
    }
}
