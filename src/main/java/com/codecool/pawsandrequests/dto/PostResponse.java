package com.codecool.pawsandrequests.dto;

import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Species;

import java.util.List;

public record PostResponse(
        String title,
        String description,
        String shelterName,
        String address,
        List<String> url,
        int age,
        Gender gender,
        Species species,
        String animalName
 ) { }
