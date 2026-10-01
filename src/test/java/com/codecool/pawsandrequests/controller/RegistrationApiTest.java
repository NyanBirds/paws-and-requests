package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.TokenResponse;
import com.codecool.pawsandrequests.mapper.UserMapper;
import com.codecool.pawsandrequests.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exercises the registration endpoint over HTTP, which is the only layer where
 * bean validation, the exception handler and the status code come together.
 *
 * <p>Standalone setup rather than a Spring context: the collaborators are
 * mocked anyway, and this keeps the suite free of a database.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("POST /api/auth/registration validation")
class RegistrationApiTest {

    private static final String PATH = "/api/auth/registration";
    private static final String TOKEN = "header.payload.signature";

    @Mock
    private AuthService authService;

    @Mock
    private UserMapper userMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthController(authService, userMapper))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private static String body(
            final String firstname,
            final String lastname,
            final String email,
            final String phonenumber,
            final String password
    ) {
        return """
                {
                  "firstname": "%s",
                  "lastname": "%s",
                  "email": "%s",
                  "phonenumber": "%s",
                  "password": "%s"
                }
                """.formatted(firstname, lastname, email, phonenumber,
                password);
    }

    private static String validBody() {
        return body("Ada", "Lovelace", "ada@example.com", "070",
                "correct-horse");
    }

    @Test
    @DisplayName("returns 201 with a token for a valid registration")
    void acceptsValidRegistration() throws Exception {
        when(authService.registration(any())).thenReturn(
                TokenResponse.bearer(TOKEN, 3600L));

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value(TOKEN));
    }

    @Test
    @DisplayName("returns 400 when the email is not email shaped")
    void rejectsMalformedEmail() throws Exception {
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("Ada", "Lovelace", "not-an-email", "070",
                                "correct-horse")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
                // containsString, not equality: a bad address trips both
                // @Email and @Pattern, and the order the two are reported in
                // is not specified.
                .andExpect(jsonPath("$.fields.email",
                        containsString("well-formed email address")))
                .andExpect(jsonPath("$.fields.email",
                        containsString("must include a domain")));

        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("returns 400 for an email whose domain has no dot")
    void rejectsEmailWithoutDottedDomain() throws Exception {
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("Ada", "Lovelace", "ada@example", "070",
                                "correct-horse")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.email").exists());

        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("returns 400 when the password is below the minimum length")
    void rejectsShortPassword() throws Exception {
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("Ada", "Lovelace", "ada@example.com",
                                "070", "short12")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.password")
                        .value("Password must be between 8 and 255 characters"));

        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("returns 400 when required fields are absent from the body")
    void rejectsMissingFields() throws Exception {
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.firstname").exists())
                .andExpect(jsonPath("$.fields.lastname").exists())
                .andExpect(jsonPath("$.fields.email").exists())
                .andExpect(jsonPath("$.fields.phonenumber").exists())
                .andExpect(jsonPath("$.fields.password").exists());

        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("returns 400 when required fields are present but empty")
    void rejectsBlankFields() throws Exception {
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("", "  ", "ada@example.com", "",
                                "correct-horse")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.firstname").exists())
                .andExpect(jsonPath("$.fields.lastname").exists())
                .andExpect(jsonPath("$.fields.phonenumber").exists());

        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("reports every offending field at once")
    void reportsAllOffendingFields() throws Exception {
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("", "", "nope", "", "short")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.length()").value(5));
    }

    @Test
    @DisplayName("keeps both messages when a field breaks two constraints")
    void mergesMessagesForTheSameField() throws Exception {
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("Ada", "Lovelace", "ada@example.com",
                                "070", "")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.password",
                        containsString("Password is required")))
                .andExpect(jsonPath("$.fields.password",
                        containsString("between 8 and 255 characters")));
    }

    @Test
    @DisplayName("does not require the optional fields")
    void doesNotRequireOptionalFields() throws Exception {
        when(authService.registration(any())).thenReturn(
                TokenResponse.bearer(TOKEN, 3600L));

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody()))
                .andExpect(status().isCreated());
    }
}
