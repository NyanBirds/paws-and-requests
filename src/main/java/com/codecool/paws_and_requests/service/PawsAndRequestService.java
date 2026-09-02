package com.codecool.paws_and_requests.service;

import com.codecool.paws_and_requests.dto.PostSummaryDto;
import com.codecool.paws_and_requests.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class PawsAndRequestService {

    private final PostRepository postRepository;

    public PawsAndRequestService(final PostRepository pr) {
        this.postRepository = pr;
    }

    public List<PostSummaryDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> new PostSummaryDto(post.getTitle(),
                        post.getDescription()))
                .toList();
    }

}



