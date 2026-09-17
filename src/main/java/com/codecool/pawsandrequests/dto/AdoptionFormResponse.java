package com.codecool.pawsandrequests.dto;

import java.util.UUID;

public record AdoptionFormResponse(
        String content,
        String firstName,
        String lastName,
        String email,
        UUID animalId
) {
}
