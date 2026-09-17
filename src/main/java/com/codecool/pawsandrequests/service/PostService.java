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
import com.codecool.pawsandrequests.repository.AnimalRepository;
import com.codecool.pawsandrequests.repository.PostRepository;
import com.codecool.pawsandrequests.repository.ShelterRepository;
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
    private final ShelterRepository shelterRepository;
    private final AnimalRepository animalRepository;
    private final PostMapper postMapper;

    public PostService(
            final PostRepository pr,
            final UserRepository ur,
            final ShelterRepository sr,
            final AnimalRepository ar,
            final PostMapper pm
    ) {
        this.postRepository = pr;
        this.userRepository = ur;
        this.shelterRepository = sr;
        this.animalRepository = ar;
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
            final String title, final String description,
            final List<String> url, final String orgNr,
            final UUID animalId, final String requesterEmail
    ) {

        // If user does not exist
        User user = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "user not found")
                );
        Shelter shelter = shelterRepository.findByOrgNr(orgNr).get();
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "animal not found"));

        // If animal is not in this shelter
        if (!animal.getShelter().getOrgNr().equals(orgNr)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "This animal doesnt belong to this shelter");
        }

        Post newPost = new Post();
        List<Picture> pictures = url.stream()
                .map(singleUrl -> {
                    Picture picture = new Picture();
                    picture.setUrl(singleUrl);
                    picture.setPost(newPost);
                    return picture;
                })
                .toList();

        // adding all elements to the newPost
        newPost.setUser(user);
        newPost.setTitle(title);
        newPost.setDescription(description);
        newPost.setAnimal(animal);
        newPost.setPictures(pictures);
        postRepository.save(newPost);

        return postMapper.toPostResponse(newPost);
    }
}
