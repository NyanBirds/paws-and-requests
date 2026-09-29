package com.codecool.pawsandrequests.mapper;

import com.codecool.pawsandrequests.dto.UserResponse;
import com.codecool.pawsandrequests.model.CustomUserDetails;
import com.codecool.pawsandrequests.model.Role;
import com.codecool.pawsandrequests.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.codecool.pawsandrequests.TestFixtures.EMAIL;
import static com.codecool.pawsandrequests.TestFixtures.admin;
import static com.codecool.pawsandrequests.TestFixtures.details;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static com.codecool.pawsandrequests.TestFixtures.shelterUser;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserMapper")
class UserMapperTest {

    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserMapperImpl();
    }

    @Test
    @DisplayName("maps the profile fields")
    void mapsProfile() {
        UUID id = UUID.randomUUID();
        User user = shelterUser(shelter());
        user.setId(id);
        user.setFirstName("Ada");
        user.setLastName("Lovelace");
        user.setPhoneNumber("070-1234567");

        UserResponse result = mapper.toUserResponse(details(user));

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.firstName()).isEqualTo("Ada");
        assertThat(result.lastName()).isEqualTo("Lovelace");
        assertThat(result.email()).isEqualTo(EMAIL);
        assertThat(result.username()).isEqualTo(EMAIL);
        assertThat(result.phoneNumber()).isEqualTo("070-1234567");
        assertThat(result.role()).isEqualTo(Role.SHELTERUSER);
    }

    @Test
    @DisplayName("never exposes the password")
    void hidesPassword() {
        User user = shelterUser(shelter());
        user.setPassword("{bcrypt}super-secret");

        UserResponse result = mapper.toUserResponse(details(user));

        assertThat(result.toString()).doesNotContain("super-secret");
    }

    @Test
    @DisplayName("returns null for a null principal")
    void nullInNullOut() {
        assertThat(mapper.toUserResponse(null)).isNull();
    }

    @Test
    @DisplayName("leaves an absent phone number null")
    void nullPhoneNumber() {
        User user = shelterUser(shelter());
        user.setPhoneNumber(null);

        assertThat(mapper.toUserResponse(details(user)).phoneNumber())
                .isNull();
    }

    @Test
    @DisplayName("maps an admin principal")
    void mapsAdmin() {
        CustomUserDetails details = details(
                admin()
        );

        assertThat(mapper.toUserResponse(details).role())
                .isEqualTo(Role.ADMIN);
    }
}
