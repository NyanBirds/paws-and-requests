package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.AnimalRequest;
import com.codecool.pawsandrequests.dto.AnimalResponse;
import com.codecool.pawsandrequests.model.CustomUserDetails;
import com.codecool.pawsandrequests.service.AnimalService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/animals")
public final class AnimalController {

    // REVIEW(sec): this endpoint is called getMyAnimals but SecurityConfig permits all requests, so it is reachable without a token. Then userDetails is null and line 28 NPEs into a 500. Two bugs with one fix: make the chain deny by default.
    private final AnimalService animalService;
// REVIEW(good): taking the identity from @AuthenticationPrincipal instead of a path variable or a query parameter is exactly the right instinct. Keep doing this everywhere: the client should never get to say which user or which shelter it is.

    public AnimalController(final AnimalService service) {
        this.animalService = service;
    }

    @GetMapping()
    public List<AnimalResponse> getMyAnimals(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ) {

        return animalService.getMyAnimals(userDetails.getOrgNr());
    }

    @GetMapping("/{id}")
    public AnimalResponse getAnimal(@PathVariable final UUID id) {
        return animalService.getAnimal(id);
    }

    @PostMapping()
    public void addAnimal(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestBody final AnimalRequest animalRequest
    ) {
        animalService.addAnimal(userDetails.getOrgNr(), animalRequest);
    }

    @DeleteMapping("/{id}")
    public void removeAnimal(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @PathVariable final UUID id
    ) {
        animalService.removeAnimal(userDetails.getOrgNr(), id);
    }
}
