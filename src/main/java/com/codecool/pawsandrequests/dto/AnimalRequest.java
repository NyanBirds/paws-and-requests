package com.codecool.pawsandrequests.dto;

public record AnimalRequest(
        String name,
        int age,
        String gender,
        String species
) {
}
