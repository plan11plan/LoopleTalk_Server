package com.demo.loopleTalk.dto.comment;

import lombok.Builder;

@Builder
public record CommentCreateRequest(
	Long postId,
	String content
) {
}
