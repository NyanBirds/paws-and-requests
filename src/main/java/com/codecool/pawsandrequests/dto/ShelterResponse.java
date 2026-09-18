package com.codecool.pawsandrequests.dto;

public record ShelterResponse(
        String orgNr,
        String shelterName,
        String address,
        String description,
        String logo
) { }
