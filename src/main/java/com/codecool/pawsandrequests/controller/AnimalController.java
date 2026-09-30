package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.AnimalRequest;
import com.codecool.pawsandrequests.dto.AnimalResponse;
import com.codecool.pawsandrequests.model.CustomUserDetails;
import com.codecool.pawsandrequests.service.AnimalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponse> getAnimal(
            @PathVariable final UUID id
    ) {
        AnimalResponse animal = animalService.getAnimal(id);
        return ResponseEntity.ok(animal);
    }

    @PostMapping()
    public ResponseEntity<AnimalResponse> addAnimal(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestBody final AnimalRequest animalRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(animalService.addAnimal(
                        userDetails.getOrgNr(), animalRequest)
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAnimal(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @PathVariable final UUID id
    ) {
        animalService.removeAnimal(userDetails.getOrgNr(), id);
        return ResponseEntity.noContent().build();
    }
}
