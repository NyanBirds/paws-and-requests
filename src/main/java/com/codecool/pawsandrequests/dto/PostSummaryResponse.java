package com.codecool.pawsandrequests.dto;

import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Species;

// REVIEW(api): the summary carries no id, so a client that lists posts has nothing to put in GET /posts/{postId}. The list endpoint is currently unusable for its actual purpose. Add the UUID.
import java.util.UUID;

public record PostSummaryResponse(
        UUID id,
        String title,
        int age,
        Gender gender,
        Species species

 ) { }
