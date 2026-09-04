package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.PostResponse;
import com.codecool.pawsandrequests.dto.PostSummaryResponse;
import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public final class PostService {

    private final PostRepository postRepository;

    public PostService(final PostRepository pr) {
        this.postRepository = pr;
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

}
