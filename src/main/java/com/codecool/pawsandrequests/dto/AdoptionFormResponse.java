package com.codecool.pawsandrequests.dto;

import com.codecool.pawsandrequests.model.Species;

import java.util.UUID;

public record AdoptionFormResponse(
        String content,
        String firstName,
        String lastName,
        String email,
        UUID animalId,
        String animalName,
        Species species
) {
}
