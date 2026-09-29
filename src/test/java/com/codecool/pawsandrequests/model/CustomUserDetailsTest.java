package com.codecool.pawsandrequests.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

import static com.codecool.pawsandrequests.TestFixtures.ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.OTHER_ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.details;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static com.codecool.pawsandrequests.TestFixtures.shelterUser;
import static com.codecool.pawsandrequests.TestFixtures.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CustomUserDetails")
class CustomUserDetailsTest {

    @Test
    @DisplayName("exposes the email as the username")
    void exposesEmailAsUsername() {
        CustomUserDetails details = details(shelterUser(shelter()));

        assertThat(details.getUsername())
                .isEqualTo(details.getEmail());
    }

    @Test
    @DisplayName("delegates the password unchanged")
    void delegatesPassword() {
        User user = shelterUser(shelter());

        assertThat(details(user).getPassword())
                .isEqualTo(user.getPassword());
    }

    @Test
    @DisplayName("exposes the shelter org number")
    void exposesOrgNumber() {
        assertThat(details(shelterUser(shelter())).getOrgNr())
                .isEqualTo(ORG_NR);
    }

    @Test
    @DisplayName("returns null when the user has no shelter")
    void throwsWithoutShelter() {
        CustomUserDetails details = details(
                user(Role.USER, null)
        );

        assertThat(details.getOrgNr()).isNull();
    }

    @Test
    @DisplayName("exposes the id, role and personal details")
    void exposesProfile() {
        UUID id = UUID.randomUUID();
        User user = shelterUser(shelter());
        user.setId(id);
        user.setFirstName("Ada");
        user.setLastName("Lovelace");
        user.setPhoneNumber("070-1234567");

        CustomUserDetails details = details(user);

        assertThat(details.getId()).isEqualTo(id);
        assertThat(details.getRole()).isEqualTo(Role.SHELTERUSER);
        assertThat(details.getFirstName()).isEqualTo("Ada");
        assertThat(details.getLastName()).isEqualTo("Lovelace");
        assertThat(details.getPhoneNumber()).isEqualTo("070-1234567");
    }

    @ParameterizedTest
    @EnumSource(Role.class)
    @DisplayName("grants exactly one ROLE_ authority")
    void grantsRoleAuthority(final Role role) {
        CustomUserDetails details = details(user(role, shelter()));

        assertThat(details.getAuthorities())
                .hasSize(1)
                .allSatisfy(authority -> assertThat(authority.getAuthority())
                        .isEqualTo("ROLE_" + role));
    }

    @Test
    @DisplayName("reports the account as fully usable")
    void reportsUsableAccount() {
        CustomUserDetails details = details(shelterUser(shelter()));

        assertThat(details.isAccountNonExpired()).isTrue();
        assertThat(details.isAccountNonLocked()).isTrue();
        assertThat(details.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @DisplayName("keeps a stable view of the wrapped user")
    void reflectsCurrentUserState() {
        User user = shelterUser(shelter());
        CustomUserDetails details = details(user);

        user.setShelter(shelter(OTHER_ORG_NR));

        assertThat(details.getOrgNr()).isEqualTo(OTHER_ORG_NR);
    }

    @Test
    @DisplayName("exposes a single SimpleGrantedAuthority per role")
    void authorityIsSimpleGrantedAuthority() {
        CustomUserDetails details = details(shelterUser(shelter()));

        GrantedAuthority authority =
                details.getAuthorities().iterator().next();

        assertThat(authority).isInstanceOf(
                org.springframework.security.core.authority
                        .SimpleGrantedAuthority.class);
    }

    @Test
    @DisplayName("returns the same authorities on every call")
    void authoritiesAreStable() {
        CustomUserDetails details = details(shelterUser(shelter()));

        assertThat(details.getAuthorities())
                .isEqualTo(details.getAuthorities());
    }
}
