package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.AdoptionFormRequest;
import com.codecool.pawsandrequests.dto.AdoptionFormResponse;
import com.codecool.pawsandrequests.model.CustomUserDetails;
import com.codecool.pawsandrequests.service.AdoptionFormService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class AdoptionFormController {
    private final AdoptionFormService adoptionFormService;

    public  AdoptionFormController(final AdoptionFormService service) {
        this.adoptionFormService = service;
    }

    @GetMapping("/posts/{postId}/adoption")
    public List<AdoptionFormResponse> getForms(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @PathVariable final UUID postId
    ) {
        return adoptionFormService.getForms(
                userDetails.getUsername(),
                postId
        );
    }

    @PostMapping("/posts/{postId}/adoption")
    public void createForm(
            @AuthenticationPrincipal final UserDetails userDetails,
            @PathVariable final UUID postId,
            @RequestBody final AdoptionFormRequest request
    ) {
        adoptionFormService.createForm(
                userDetails.getUsername(),
                postId,
                request
        );
    }
}
