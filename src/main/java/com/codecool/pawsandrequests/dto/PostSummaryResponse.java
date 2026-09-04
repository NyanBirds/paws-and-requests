package com.codecool.pawsandrequests.dto;

import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Species;

public record PostSummaryResponse(
        String title,
        int age,
        Gender gender,
        Species species

 ) { }
