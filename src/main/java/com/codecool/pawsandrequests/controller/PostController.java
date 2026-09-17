package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.PostRequest;
import com.codecool.pawsandrequests.dto.PostResponse;
import com.codecool.pawsandrequests.dto.PostSummaryResponse;
import com.codecool.pawsandrequests.model.CustomUserDetails;
import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Species;
import org.springframework.security.core.userdetails.UserDetails;
import com.codecool.pawsandrequests.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping()
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal final CustomUserDetails customUserDetails,
            @RequestBody final PostRequest postRequest) {
        PostResponse response = service.createPost(postRequest.title(),
                postRequest.description(),
                postRequest.url(),
                customUserDetails.getOrgNr(),
                postRequest.animalId(),
                customUserDetails.getUsername()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal final UserDetails userDetails,
            @PathVariable final UUID postId) {
        service.deletePost(postId,  userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
