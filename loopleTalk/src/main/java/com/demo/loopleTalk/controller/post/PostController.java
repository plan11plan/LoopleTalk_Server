package com.demo.loopleTalk.controller.post;


import com.demo.loopleTalk.dto.post.CreatePostRequest;
import com.demo.loopleTalk.service.post.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody CreatePostRequest request) {
        postService.create(request);
    }
}
