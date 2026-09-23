package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.PostRequest;
import com.codecool.pawsandrequests.dto.PostResponse;
import com.codecool.pawsandrequests.dto.PostSummaryResponse;
import com.codecool.pawsandrequests.model.CustomUserDetails;
import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Species;
import com.codecool.pawsandrequests.service.PostService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
public final class PostController {

    private final PostService postService;

    public PostController(
            final PostService pService
    ) {
        this.postService = pService;
    }

    @GetMapping()
    public List<PostSummaryResponse> getAllPosts(
            final @RequestParam(required = false) List<Gender> gender,
            final @RequestParam(required = false) List<Species> species
    ) {
        return postService.getAllPosts(gender, species);
    }

    @GetMapping("/{postId}")
    public PostResponse getOnePost(@PathVariable final UUID postId) {
        return postService.getOnePost(postId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal final CustomUserDetails customUserDetails,
            @RequestPart("post") final PostRequest postRequest,
            @RequestPart(value = "pictures", required = false)
            final List<MultipartFile> pictures) {
        PostResponse response = postService.createPost(postRequest, pictures,
                customUserDetails.getOrgNr(),
                customUserDetails.getUsername()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal final UserDetails userDetails,
            @PathVariable final UUID postId) {
        postService.deletePost(postId,  userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
