package com.codecool.pawsandrequests.dto;

import com.codecool.pawsandrequests.model.Role;

public record UserResponse(
        String username,
        Role role
) {
}
