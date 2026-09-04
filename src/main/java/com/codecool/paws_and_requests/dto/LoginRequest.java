package com.codecool.paws_and_requests.dto;

public record LoginRequest(
        String email,
        String password
) {
}
