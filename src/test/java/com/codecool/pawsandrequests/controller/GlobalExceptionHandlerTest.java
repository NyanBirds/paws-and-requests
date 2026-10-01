package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.RegistrationRequest;
import com.codecool.pawsandrequests.dto.ValidationErrorResponse;
import com.codecool.pawsandrequests.exception.EmailTakenException;
import com.codecool.pawsandrequests.exception.ShelterNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Method;
import java.util.List;

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

    private static MethodArgumentNotValidException validationFailure(
            final List<FieldError> errors
    ) throws NoSuchMethodException {
        RegistrationRequest request = new RegistrationRequest("Ada",
                "Lovelace", "ada@example.com", "070", "correct-horse", null,
                null);
        MethodParameter parameter = new MethodParameter(
                AuthController.class.getMethod("registration",
                        RegistrationRequest.class), 0);
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(request, "registrationRequest");
        errors.forEach(bindingResult::addError);
        return new MethodArgumentNotValidException(parameter, bindingResult);
    }

    private static FieldError fieldError(final String field,
                                         final String message) {
        return new FieldError("registrationRequest", field, "rejected", false,
                new String[]{"rejected"}, null, message);
    }

    @Test
    @DisplayName("maps a validation failure to 400 with the offending fields")
    void mapsValidationFailureToBadRequest() throws NoSuchMethodException {
        MethodArgumentNotValidException ex = validationFailure(List.of(
                fieldError("email", "Email must be a well-formed email address"),
                fieldError("password", "Password is required")));

        ResponseEntity<ValidationErrorResponse> response =
                handler.handleValidation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ValidationErrorResponse body = response.getBody();
        assert body != null;
        assertThat(body.code()).isEqualTo("VALIDATION_FAILED");
        assertThat(body.fields())
                .containsEntry("email",
                        "Email must be a well-formed email address")
                .containsEntry("password", "Password is required");
    }

    @Test
    @DisplayName("concatenates both messages when a field breaks two rules")
    void mergesMessagesForTheSameField() throws NoSuchMethodException {
        MethodArgumentNotValidException ex = validationFailure(List.of(
                fieldError("password", "Password is required"),
                fieldError("password", "Password must be at least 8")));

        ValidationErrorResponse body = handler.handleValidation(ex).getBody();

        assert body != null;
        assertThat(body.fields())
                .containsEntry("password",
                        "Password is required Password must be at least 8");
    }

    @Test
    @DisplayName("sorts fields by name so the body is stable")
    void sortsFieldsByName() throws NoSuchMethodException {
        MethodArgumentNotValidException ex = validationFailure(List.of(
                fieldError("password", "last"),
                fieldError("email", "middle"),
                fieldError("firstname", "first")));

        ValidationErrorResponse body = handler.handleValidation(ex).getBody();

        assert body != null;
        assertThat(body.fields().keySet()).containsExactly("email",
                "firstname", "password");
    }
}
