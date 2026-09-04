package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.PostSummaryDto;
import com.codecool.pawsandrequests.service.PawsAndRequestService;
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
