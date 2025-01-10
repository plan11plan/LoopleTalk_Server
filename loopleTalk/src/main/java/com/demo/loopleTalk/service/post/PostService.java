package com.demo.loopleTalk.service.post;

import com.demo.loopleTalk.domain.post.Post;
import com.demo.loopleTalk.dto.post.CreatePostRequest;
import com.demo.loopleTalk.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public void create(CreatePostRequest request) {
        Post createdPost = Post.builder()
                .profileId(request.profileId())
                .content(request.content())
                .longitude(request.longitude())
                .latitude(request.latitude())
                .build();
        postRepository.save(createdPost);
    }

}
