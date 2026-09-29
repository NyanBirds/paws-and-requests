package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.UserRequest;
import com.codecool.pawsandrequests.mapper.UserMapper;
import com.codecool.pawsandrequests.model.Role;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.model.User;
import com.codecool.pawsandrequests.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static com.codecool.pawsandrequests.TestFixtures.EMAIL;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static com.codecool.pawsandrequests.TestFixtures.shelterUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService")
class UserServiceTest {

    private static final String ENCODED = "{bcrypt}encoded";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, userMapper,
                passwordEncoder);
    }

    @Test
    @DisplayName("encodes and stores a new password")
    void encodesNewPassword() {
        User user = shelterUser(shelter());
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("hunter2")).thenReturn(ENCODED);

        userService.editUser(EMAIL, UUID.randomUUID(),
                new UserRequest("hunter2", "070-9999999"));

        assertThat(user.getPassword()).isEqualTo(ENCODED);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("never stores the raw password")
    void neverStoresRawPassword() {
        User user = shelterUser(shelter());
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("hunter2")).thenReturn(ENCODED);

        userService.editUser(EMAIL, UUID.randomUUID(),
                new UserRequest("hunter2", null));

        assertThat(user.getPassword()).isNotEqualTo("hunter2");
    }

    @Test
    @DisplayName("updates the phone number when provided")
    void updatesPhoneNumber() {
        User user = shelterUser(shelter());
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        userService.editUser(EMAIL, UUID.randomUUID(),
                new UserRequest(null, "070-0000000"));

        assertThat(user.getPhoneNumber()).isEqualTo("070-0000000");
    }

    @Test
    @DisplayName("leaves the password untouched when null")
    void keepsPasswordWhenNull() {
        User user = shelterUser(shelter());
        String original = user.getPassword();
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        userService.editUser(EMAIL, UUID.randomUUID(),
                new UserRequest(null, "070-0000000"));

        assertThat(user.getPassword()).isEqualTo(original);
        verify(passwordEncoder, never()).encode(org.mockito.ArgumentMatchers
                .any());
    }

    @Test
    @DisplayName("leaves the phone number untouched when null")
    void keepsPhoneWhenNull() {
        User user = shelterUser(shelter());
        String original = user.getPhoneNumber();
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        userService.editUser(EMAIL, UUID.randomUUID(),
                new UserRequest(null, null));

        assertThat(user.getPhoneNumber()).isEqualTo(original);
    }

    @Test
    @DisplayName("leaves the role and shelter untouched")
    void doesNotEscalatePrivileges() {
        Shelter shelter = shelter();
        User user = shelterUser(shelter);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        userService.editUser(EMAIL, UUID.randomUUID(),
                new UserRequest(null, "070-0000000"));

        assertThat(user.getRole()).isEqualTo(Role.SHELTERUSER);
        assertThat(user.getShelter()).isSameAs(shelter);
    }

    @Test
    @DisplayName("still saves when nothing was changed")
    void savesUnchangedUser() {
        User user = shelterUser(shelter());
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        userService.editUser(EMAIL, UUID.randomUUID(),
                new UserRequest(null, null));

        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("rejects an unknown email")
    void rejectsUnknownEmail() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.editUser(EMAIL,
                UUID.randomUUID(), new UserRequest(null, null)))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");

        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("looks the user up by the caller's email, not the path id")
    void ignoresPathId() {
        User user = shelterUser(shelter());
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UUID pathId = UUID.randomUUID();

        userService.editUser(EMAIL, pathId, new UserRequest(null, null));

        verify(userRepository).findByEmail(EMAIL);
        verify(userRepository).save(user);
    }
}
