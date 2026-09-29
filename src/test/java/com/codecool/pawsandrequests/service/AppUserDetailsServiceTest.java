package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.model.CustomUserDetails;
import com.codecool.pawsandrequests.model.User;
import com.codecool.pawsandrequests.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.codecool.pawsandrequests.TestFixtures.EMAIL;
import static com.codecool.pawsandrequests.TestFixtures.ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static com.codecool.pawsandrequests.TestFixtures.shelterUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AppUserDetailsService")
class AppUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppUserDetailsService userDetailsService;

    @Test
    @DisplayName("wraps the stored user in CustomUserDetails")
    void wrapsStoredUser() {
        User user = shelterUser(shelter());
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        UserDetails details = userDetailsService.loadUserByUsername(EMAIL);

        assertThat(details).isInstanceOf(CustomUserDetails.class);
        assertThat(details.getUsername()).isEqualTo(EMAIL);
        assertThat(details.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @DisplayName("carries the shelter org number through")
    void carriesShelterOrgNumber() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(
                Optional.of(shelterUser(shelter()))
        );

        CustomUserDetails details = (CustomUserDetails)
                userDetailsService.loadUserByUsername(EMAIL);

        assertThat(details.getOrgNr()).isEqualTo(ORG_NR);
    }

    @Test
    @DisplayName("throws when no user matches the email")
    void throwsForUnknownEmail() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(EMAIL))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found: " + EMAIL);
    }

    @Test
    @DisplayName("maps the role to a ROLE_ authority")
    void mapsRoleToAuthority() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(
                shelterUser(shelter())
        ));

        UserDetails details = userDetailsService.loadUserByUsername(EMAIL);

        assertThat(details.getAuthorities()).hasSize(1)
                .allSatisfy(authority -> assertThat(authority.getAuthority())
                        .isEqualTo("ROLE_SHELTERUSER"));
    }
}
