package com.demo.loopleTalk.dto.comment;

import lombok.Builder;

@Builder
public record CommentUpdateDto(
	Long postId,
	String content
) {
}
