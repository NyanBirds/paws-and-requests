package com.codecool.paws_and_requests.controller;

import com.codecool.paws_and_requests.dto.PostSummaryDto;
import com.codecool.paws_and_requests.service.PawsAndRequestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public final class PawsAndRequestController {

    private final PawsAndRequestService service;

    public PawsAndRequestController(final PawsAndRequestService s) {
        this.service = s;
    }

    @GetMapping("/pawsandrequest/posts")
    public List<PostSummaryDto> getAllPosts() {
        return service.getAllPosts();
    }
}
