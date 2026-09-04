package com.codecool.paws_and_requests.dto;

public record RegistrationRequest(
        String firstname,
        String lastname,
        String email,
        String phonenumber,
        String password,
        String profilePicture,
        String shelterOrg
) {
}
