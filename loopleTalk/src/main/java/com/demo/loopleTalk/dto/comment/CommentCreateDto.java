package com.demo.loopleTalk.dto.comment;

import lombok.Builder;

@Builder
public record CommentCreateDto(
	Long postId,
	String content
) {
}
