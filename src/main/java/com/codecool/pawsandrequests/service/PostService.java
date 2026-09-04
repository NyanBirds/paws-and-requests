package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.PostSummaryDto;
import com.codecool.pawsandrequests.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class PostService {

    private final PostRepository postRepository;

    public PostService(final PostRepository pr) {
        this.postRepository = pr;
    }

    public List<PostSummaryDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> new PostSummaryDto(post.getTitle(),
                        post.getDescription()))
                .toList();
    }
}
