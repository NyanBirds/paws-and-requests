package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.PostResponse;
import com.codecool.pawsandrequests.dto.PostSummaryResponse;
import com.codecool.pawsandrequests.mapper.PostMapper;
import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.model.Species;
import com.codecool.pawsandrequests.model.User;
import com.codecool.pawsandrequests.repository.PostRepository;
import com.codecool.pawsandrequests.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public final class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    public PostService(
            final PostRepository pr,
            // REVIEW(efficiency): classic N+1. findAll() runs one query, then post.getAnimal() lazily fires another per post. Use a @Query with a join fetch, or better a projection interface, so this is one round trip.
            final UserRepository ur,
            final PostMapper pm
    ) {
        this.postRepository = pr;
        this.userRepository = ur;
        this.postMapper = pm;
    }

    public List<PostSummaryResponse> getAllPosts(
            final Gender gender,
            final Species species
    ) {

        if (gender != null && species != null) {
            return postRepository
                    .findByAnimalGenderAndAnimalSpecies(gender, species)
                    .stream()
                    .map(postMapper::toPostSummaryResponse)
                    .toList();
        }

        // REVIEW(efficiency): same N+1 for pictures, plus animal and shelter. One post is cheap enough that this is fine, but know that it is three queries.
        if (gender != null) {
            return postRepository
                    .findByAnimalGender(gender)
                    .stream()
                    .map(postMapper::toPostSummaryResponse)
                    .toList();
        }

        if (species != null) {
            return postRepository
                    .findByAnimalSpecies(species)
                    .stream()
                    .map(postMapper::toPostSummaryResponse)
                    .toList();
        }

        return postRepository.findAll().stream()
                .map(postMapper::toPostSummaryResponse)
                // REVIEW(api): the service re-loads the user by email although the filter already loaded it. Passing CustomUserDetails (which holds the User) would save the query.
                .toList();
    }

    public PostResponse getOnePost(final UUID postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "post not found")
                );

        return postMapper.toPostResponse(post);
    }

// REVIEW(good): the ownership check is here, in the service, and it compares the requester's shelter to the post's shelter rather than trusting anything from the request. This is the right pattern and it is what the other repos are missing.
// REVIEW(noob): a user whose role is SHELTER but whose shelter is null falls into the first branch and gets a 403 with a misleading message. Minor, but worth a distinct message.

    public void deletePost(final UUID postId, final String requesterEmail) {

        // If post does not exist
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.
                        NOT_FOUND, "post not found")
                );
        // If user does not exist
        User user = userRepository.findByEmail(requesterEmail)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "user not found")
                        );

        switch (user.getRole()) {
            case ADMIN ->  { }        // admin can delete any post
            case SHELTERUSER -> {
                if (user.getShelter() == null
                        || !user.getShelter().getOrgNr().equals(
                                post.getAnimal().getShelter().getOrgNr()
                        )) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "You can only delete posts from your own shelter");
                }
            }
            default -> throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only shelter staff or admins can delete post");
        }
        postRepository.delete(post);
    }

}
