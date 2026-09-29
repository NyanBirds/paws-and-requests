package com.codecool.pawsandrequests.dto;

public record ShelterRequest(
        String orgNr,
        String shelterName,
        String address,
        String description
) { }
