package com.demo.loopleTalk.dto.post;


public record CreatePostRequest(
        long profileId,

        String content,
        double longitude,
        double latitude
) {
}
