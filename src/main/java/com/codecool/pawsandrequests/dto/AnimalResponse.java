package com.codecool.pawsandrequests.dto;

import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Species;

import java.util.UUID;

public record AnimalResponse(
        UUID id,
        String name,
        int age,
        Gender gender,
        Species species
) {
}
