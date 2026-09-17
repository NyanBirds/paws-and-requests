package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.ShelterResponse;
import com.codecool.pawsandrequests.service.ShelterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
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
    public ShelterResponse getShelter(@PathVariable final String orgNr) {
        return service.getShelter(orgNr);
    }
}
