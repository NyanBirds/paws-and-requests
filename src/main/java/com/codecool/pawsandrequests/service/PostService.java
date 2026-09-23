package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.PostRequest;
import com.codecool.pawsandrequests.dto.PostResponse;
import com.codecool.pawsandrequests.dto.PostSummaryResponse;
import com.codecool.pawsandrequests.mapper.PostMapper;
import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.Species;
import com.codecool.pawsandrequests.model.User;
import com.codecool.pawsandrequests.repository.AnimalRepository;
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
    private final AnimalRepository animalRepository;
    private final PostMapper postMapper;

    public PostService(
            final PostRepository pr,
            final UserRepository ur,
            final AnimalRepository ar,
            final PostMapper pm
    ) {
        this.postRepository = pr;
        this.userRepository = ur;
        this.animalRepository = ar;
        this.postMapper = pm;

    }

    public List<PostSummaryResponse> getAllPosts(
            final List<Gender> gender,
            final List<Species> species
    ) {

        if (gender != null && species != null) {
            return postRepository
                    .findByAnimalGenderInAndAnimalSpeciesIn(gender, species)
                    .stream()
                    .map(postMapper::toPostSummaryResponse)
                    .toList();
        }

        if (gender != null) {
            return postRepository
                    .findByAnimalGenderIn(gender)
                    .stream()
                    .map(postMapper::toPostSummaryResponse)
                    .toList();
        }

        if (species != null) {
            return postRepository
                    .findByAnimalSpeciesIn(species)
                    .stream()
                    .map(postMapper::toPostSummaryResponse)
                    .toList();
        }

        return postRepository.findAll().stream()
                .map(postMapper::toPostSummaryResponse)
                .toList();
    }

    public PostResponse getOnePost(final UUID postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "post not found")
                );

        return postMapper.toPostResponse(post);
    }


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

    public PostResponse createPost(
            final PostRequest postRequest,
            final String orgNr,
            final String requesterEmail
    ) {

        // If user does not exist
        User user = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "user not found")
                );
        Animal animal = animalRepository.findById(postRequest.animalId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "animal not found"));

        // If animal is not in this shelter
        if (!animal.getShelter().getOrgNr().equals(orgNr)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "This animal doesnt belong to this shelter");
        }
        Post newPost = postMapper.toPost(postRequest, user, animal);
        List<Picture> pictures = postRequest.url().stream()
                .map(singleUrl -> {
                    Picture picture = new Picture();
                    picture.setUrl(singleUrl);
                    picture.setPost(newPost);
                    return picture;
                })
                .toList();

        newPost.setPictures(pictures);

        postRepository.save(newPost);

        return postMapper.toPostResponse(newPost);
    }
}
