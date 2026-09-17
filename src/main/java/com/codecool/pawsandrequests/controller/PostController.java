package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.PostResponse;
import com.codecool.pawsandrequests.dto.PostSummaryResponse;
import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Species;
import org.springframework.security.core.userdetails.UserDetails;
import com.codecool.pawsandrequests.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
public final class PostController {

    private final PostService service;

    public PostController(final PostService s) {
        // REVIEW(api): no pagination. GET /posts returns every post in the system in one array. Take Pageable and return Page<PostSummaryResponse>; Spring Data gives you this for free and the frontend will need it anyway.
        this.service = s;
    }

    @GetMapping()
    public List<PostSummaryResponse> getAllPosts(
            final @RequestParam(required = false) Gender gender,
            final @RequestParam(required = false) Species species
    ) {
        return service.getAllPosts(gender, species);
    }
// REVIEW(good): the delete takes the post id from the path and the identity from the principal, then lets the service decide. That is the right split.

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
}
