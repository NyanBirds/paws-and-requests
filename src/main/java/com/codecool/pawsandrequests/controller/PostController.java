package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.PostResponse;
import com.codecool.pawsandrequests.dto.PostSummaryResponse;
import com.codecool.pawsandrequests.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
public final class PostController {

    private final PostService service;

    public PostController(final PostService s) {
        this.service = s;
    }

    @GetMapping()
    public List<PostSummaryResponse> getAllPosts() {
        return service.getAllPosts();
    }

    @GetMapping("/{postId}")
    public PostResponse getOnePost(@PathVariable final UUID postId) {
        return service.getOnePost(postId);
    }


}
