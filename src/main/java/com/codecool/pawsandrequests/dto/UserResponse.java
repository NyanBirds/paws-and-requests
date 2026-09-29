package com.codecool.pawsandrequests.dto;

import com.codecool.pawsandrequests.model.Role;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        Role role,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String orgNr
) {
}
