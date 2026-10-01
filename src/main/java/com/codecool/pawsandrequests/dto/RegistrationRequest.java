package com.codecool.pawsandrequests.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Body of {@code POST /api/auth/registration}.
 *
 * <p>Every constraint carries an explicit message. The messages are written
 * out rather than pulled from a resource bundle so the text a client receives
 * is fixed in one place, and so it does not depend on an EL implementation
 * being on the classpath for interpolation.
 *
 * <p>{@code @Email} accepts {@code null} and the empty string, so it is always
 * paired with {@code @NotBlank} to make "absent" fail as well as "malformed".
 *
 * <p>{@code profilePicture} and {@code shelterOrg} are optional: a plain user
 * registration sends neither, and {@code shelterOrg} only decides whether the
 * account becomes a {@code SHELTERUSER}.
 */
public record RegistrationRequest(
        @NotBlank(message = "First name is required")
        @Size(max = MAX_NAME_LENGTH,
                message = "First name must be at most 255 characters")
        String firstname,

        @NotBlank(message = "Last name is required")
        @Size(max = MAX_NAME_LENGTH,
                message = "Last name must be at most 255 characters")
        String lastname,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a well-formed email address")
        @Pattern(regexp = DOMAIN_REQUIRED,
                message = "Email must include a domain, like "
                        + "name@example.com")
        @Size(max = EMAIL_MAX_LENGTH,
                message = "Email must be at most 255 characters")
        String email,

        @NotBlank(message = "Phone number is required")
        @Size(max = PHONE_MAX_LENGTH,
                message = "Phone number must be at most 255 characters")
        String phonenumber,

        @NotBlank(message = "Password is required")
        @Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH,
                message = "Password must be between 8 and 255 characters")
        String password,

        String profilePicture,

        String shelterOrg
) {

    /**
     * Column widths for the {@code users} table. These mirror the
     * {@code character varying(255)} columns declared in
     * {@code V1__create_schema.sql} and Hibernate's default string length, so
     * validation rejects the same values the database would.
     *
     * <p>They are constants rather than literals in the annotations because
     * Checkstyle's {@code MagicNumber} check is configured to inspect
     * annotation arguments, which fails the build on a bare {@code 255}.
     */
    public static final int MAX_NAME_LENGTH = 255;

    /** Maximum accepted email length, matching {@code users.email}. */
    public static final int EMAIL_MAX_LENGTH = 255;

    /**
     * Maximum accepted phone number length, matching
     * {@code users.phone_number}.
     */
    public static final int PHONE_MAX_LENGTH = 255;

    /**
     * Shortest accepted password. Eight is the floor that keeps the documented
     * demo credential ({@code password}) working.
     */
    public static final int MIN_PASSWORD_LENGTH = 8;

    /** Maximum accepted password length, matching the stored hash column. */
    public static final int MAX_PASSWORD_LENGTH = 255;

    /**
     * Requires a dotted domain, which the stock {@code @Email} does not.
     *
     * <p>Hibernate's built-in address check only insists on
     * {@code something@something}, so it accepts {@code ada@example}. A mail
     * server would reject that, which means the account could never receive a
     * password reset. This pattern closes the gap without pulling in a
     * dependency: exactly one {@code @}, no whitespace, and at least one dot
     * after the {@code @}.
     */
    public static final String DOMAIN_REQUIRED =
            "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
}
