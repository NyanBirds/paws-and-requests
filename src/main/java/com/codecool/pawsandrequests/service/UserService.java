package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.UserRequest;
import com.codecool.pawsandrequests.mapper.UserMapper;
import com.codecool.pawsandrequests.model.User;
import com.codecool.pawsandrequests.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            final UserRepository repository,
            final UserMapper mapper,
            final PasswordEncoder encoder
    ) {
        this.userRepository = repository;
        this.passwordEncoder = encoder;
    }

    public final void editUser(
            final String email,
            final UUID id,
            final UserRequest request
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        if (request.password() != null) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        if (request.phoneNumber() != null) {
            user.setPhoneNumber(request.phoneNumber());
        }

        userRepository.save(user);
    }
}
