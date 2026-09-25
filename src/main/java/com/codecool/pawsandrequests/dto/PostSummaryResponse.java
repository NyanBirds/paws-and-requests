package com.codecool.pawsandrequests.dto;

import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Species;

import java.util.Optional;
import java.util.UUID;

public record PostSummaryResponse(
        UUID id,
        String title,
        int age,
        Gender gender,
        Species species,
        Optional<String> url
 ) { }
