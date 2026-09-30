package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.ShelterRequest;
import com.codecool.pawsandrequests.dto.ShelterResponse;
import com.codecool.pawsandrequests.service.ShelterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/shelters")
public final class ShelterController {
    private final ShelterService service;

    public ShelterController(final ShelterService s) {
        this.service = s;
    }

    @GetMapping
    public List<ShelterResponse> getShelters() {
        return service.getAllShelters();
    }

    @GetMapping("/{orgNr}")
    public ResponseEntity<ShelterResponse> getShelter(
            @PathVariable final String orgNr
    ) {
        ShelterResponse shelter = service.getShelter(orgNr);
        return ResponseEntity.ok(shelter);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ShelterResponse> createShelter(
            @AuthenticationPrincipal final UserDetails userDetails,
            @RequestPart("shelter") final ShelterRequest request,
            @RequestPart(value = "picture", required = false)
            final MultipartFile picture) {
        ShelterResponse response = service.createShelter(
                request, picture, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(value = "/{orgNr}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ShelterResponse editShelter(
            @AuthenticationPrincipal final UserDetails userDetails,
            @PathVariable final String orgNr,
            @RequestPart("shelter") final ShelterRequest request,
            @RequestPart(value = "picture", required = false)
            final MultipartFile picture) {
        return service.updateShelter(
                orgNr, request, picture, userDetails.getUsername());
    }

    @DeleteMapping("/{orgNr}")
    public ResponseEntity<Void> deleteShelter(
            @AuthenticationPrincipal final UserDetails userDetails,
            @PathVariable final String orgNr) {
        service.deleteShelter(orgNr, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
