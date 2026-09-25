package com.codecool.pawsandrequests.dto;

import java.util.Optional;

public record ShelterResponse(
        String orgNr,
        String shelterName,
        String address,
        String description,
        Optional<String> url
) { }
