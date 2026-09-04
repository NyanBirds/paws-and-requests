package com.codecool.pawsandrequests.dto;

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
