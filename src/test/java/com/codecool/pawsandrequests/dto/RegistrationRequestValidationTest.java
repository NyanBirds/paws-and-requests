package com.codecool.pawsandrequests.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pins the validation rules {@code RegistrationRequest} declares, independent
 * of Spring. {@code RegistrationApiTest} covers the HTTP side.
 */
@DisplayName("RegistrationRequest validation")
class RegistrationRequestValidationTest {

    private static final String EMAIL = "ada@example.com";
    private static final String PASSWORD = "correct-horse";

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        factory.close();
    }

    private static RegistrationRequest valid() {
        return new RegistrationRequest("Ada", "Lovelace", EMAIL, "070",
                PASSWORD, null, null);
    }

    private static RegistrationRequest withEmail(final String email) {
        return new RegistrationRequest("Ada", "Lovelace", email, "070",
                PASSWORD, null, null);
    }

    private static RegistrationRequest withPassword(final String password) {
        return new RegistrationRequest("Ada", "Lovelace", EMAIL, "070",
                password, null, null);
    }

    private static RegistrationRequest with(
            final String firstname,
            final String lastname,
            final String phonenumber
    ) {
        return new RegistrationRequest(firstname, lastname, EMAIL, phonenumber,
                PASSWORD, null, null);
    }

    private static Set<String> rejectedFields(
            final RegistrationRequest request
    ) {
        return validator.validate(request).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(java.util.stream.Collectors.toSet());
    }

    private static Set<String> messagesFor(final RegistrationRequest request,
                                           final String field) {
        return validator.validate(request).stream()
                .filter(violation -> violation.getPropertyPath().toString()
                        .equals(field))
                .map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());
    }

    @Test
    @DisplayName("accepts a complete, well-formed request")
    void acceptsValidRequest() {
        assertThat(validator.validate(valid())).isEmpty();
    }

    @Test
    @DisplayName("accepts a shelter registration that names an organisation")
    void acceptsShelterRegistration() {
        RegistrationRequest request = new RegistrationRequest("Ada",
                "Lovelace", EMAIL, "070", PASSWORD, null, "556677-8899");

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    @DisplayName("rejects a missing first name")
    void rejectsMissingFirstname() {
        assertThat(rejectedFields(with(null, "Lovelace", "070")))
                .contains("firstname");
        assertThat(rejectedFields(with("   ", "Lovelace", "070")))
                .contains("firstname");
    }

    @Test
    @DisplayName("rejects a missing last name")
    void rejectsMissingLastname() {
        assertThat(rejectedFields(with("Ada", null, "070")))
                .contains("lastname");
        assertThat(rejectedFields(with("Ada", "  ", "070")))
                .contains("lastname");
    }

    @Test
    @DisplayName("rejects a missing phone number")
    void rejectsMissingPhonenumber() {
        assertThat(rejectedFields(with("Ada", "Lovelace", null)))
                .contains("phonenumber");
    }

    @Test
    @DisplayName("rejects a missing email as required, not just malformed")
    void rejectsMissingEmail() {
        assertThat(rejectedFields(withEmail(null))).contains("email");
        assertThat(rejectedFields(withEmail(""))).contains("email");
    }

    @Test
    @DisplayName("rejects an email that is not email shaped")
    void rejectsMalformedEmail() {
        assertThat(rejectedFields(withEmail("not-an-email")))
                .contains("email");
        assertThat(rejectedFields(withEmail("ada example.com")))
                .contains("email");
        assertThat(rejectedFields(withEmail("@example.com")))
                .contains("email");
        assertThat(rejectedFields(withEmail("ada@@example.com")))
                .contains("email");
    }

    @Test
    @DisplayName("rejects an email whose domain has no dot")
    void rejectsEmailWithoutDottedDomain() {
        // The stock @Email accepts this, which is why DOMAIN_REQUIRED exists.
        assertThat(rejectedFields(withEmail("ada@example")))
                .contains("email");
        assertThat(rejectedFields(withEmail("ada@localhost")))
                .contains("email");
    }

    @Test
    @DisplayName("accepts a dotted email with a subdomain")
    void acceptsDottedEmail() {
        assertThat(validator.validate(withEmail("ada@mail.example.com")))
                .isEmpty();
    }

    @Test
    @DisplayName("rejects a password shorter than the minimum length")
    void rejectsShortPassword() {
        assertThat(rejectedFields(withPassword("short12")))
                .contains("password");
        assertThat(rejectedFields(withPassword("1234567")))
                .contains("password");
    }

    @Test
    @DisplayName("accepts a password of exactly the minimum length")
    void acceptsPasswordAtMinimumLength() {
        assertThat(validator.validate(withPassword("password"))).isEmpty();
    }

    @Test
    @DisplayName("rejects a missing password")
    void rejectsMissingPassword() {
        assertThat(rejectedFields(withPassword(null))).contains("password");
    }

    @Test
    @DisplayName("reports every offending field in one pass")
    void reportsAllOffendingFields() {
        RegistrationRequest request = new RegistrationRequest(null, null,
                "nope", null, "short", null, null);

        assertThat(rejectedFields(request)).containsExactlyInAnyOrder(
                "firstname", "lastname", "email", "phonenumber", "password");
    }

    @Test
    @DisplayName("leaves the optional fields unconstrained")
    void ignoresOptionalFields() {
        RegistrationRequest request = new RegistrationRequest("Ada",
                "Lovelace", EMAIL, "070", PASSWORD, null, null);

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    @DisplayName("names the offending field in the message source")
    void exposesFieldMessages() {
        assertThat(messagesFor(with("Ada", "Lovelace", null), "phonenumber"))
                .containsExactly("Phone number is required");
        assertThat(messagesFor(withPassword("short"), "password"))
                .containsExactly(
                        "Password must be between 8 and 255 characters");
        assertThat(messagesFor(withEmail("nope"), "email"))
                .contains("Email must be a well-formed email address",
                        "Email must include a domain, like name@example.com");
    }
}
