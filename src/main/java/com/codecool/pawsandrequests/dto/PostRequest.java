package com.codecool.pawsandrequests.dto;

import java.util.List;
import java.util.UUID;

public record PostRequest(
        String title,
        String description,
        UUID animalId
) { }
