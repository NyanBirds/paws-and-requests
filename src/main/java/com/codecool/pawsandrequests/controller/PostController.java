package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.PostResponse;
import com.codecool.pawsandrequests.dto.PostSummaryResponse;
import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Species;
import org.springframework.security.core.userdetails.UserDetails;
import com.codecool.pawsandrequests.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public List<PostSummaryResponse> getAllPosts(
            final @RequestParam(required = false) Gender gender,
            final @RequestParam(required = false) Species species
    ) {
        return service.getAllPosts(gender, species);
    }

    @GetMapping("/{postId}")
    public PostResponse getOnePost(@PathVariable final UUID postId) {
        return service.getOnePost(postId);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal final UserDetails userDetails,
            @PathVariable final UUID postId) {
        service.deletePost(postId,  userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shelter/{orgNr}")
    public List<PostSummaryResponse> getShelterPosts(
            @PathVariable final String orgNr) {
        return service.getShelterPosts(orgNr);
    }
}
