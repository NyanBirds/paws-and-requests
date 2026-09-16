package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.AnimalResponse;
import com.codecool.pawsandrequests.model.CustomUserDetails;
import com.codecool.pawsandrequests.service.AnimalService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/animals")
public final class AnimalController {

    private final AnimalService animalService;

    public AnimalController(final AnimalService service) {
        this.animalService = service;
    }

    @GetMapping()
    public List<AnimalResponse> getMyAnimals(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ) {

        return animalService.getMyAnimals(userDetails.getOrgNr());
    }
}
