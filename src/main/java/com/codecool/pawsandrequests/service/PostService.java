package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.PostResponse;
import com.codecool.pawsandrequests.dto.PostSummaryResponse;
import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.Shelter;
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

    public PostService(final PostRepository pr, final UserRepository ur) {
        this.postRepository = pr;
        this.userRepository = ur;
    }

    public List<PostSummaryResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post1 -> new PostSummaryResponse(
                        post1.getTitle(),
                        post1.getAnimal().getAge(),
                        post1.getAnimal().getGender(),
                        post1.getAnimal().getSpecies()))
                .toList();


    }

    public PostResponse getOnePost(final UUID postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "post not found")
                );

        Animal animal = post.getAnimal();
        Shelter shelter = animal.getShelter();

        List<String> pictureUrls = post.getPictures().stream()
                .map(Picture::getUrl)
                .toList();

        return new PostResponse(
                post.getTitle(),
                post.getDescription(),
                shelter.getShelterName(),
                shelter.getAddress(),
                pictureUrls,
                animal.getAge(),
                animal.getGender(),
                animal.getSpecies(),
                animal.getName()
        );
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
            case SHELTER -> {
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
