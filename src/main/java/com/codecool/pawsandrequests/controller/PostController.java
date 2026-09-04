package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.PostSummaryDto;
import com.codecool.pawsandrequests.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/posts")
public final class PostController {

    private final PostService service;

    public PostController(final PostService s) {
        this.service = s;
    }

    @GetMapping()
    public List<PostSummaryDto> getAllPosts() {
        return service.getAllPosts();
    }
}
