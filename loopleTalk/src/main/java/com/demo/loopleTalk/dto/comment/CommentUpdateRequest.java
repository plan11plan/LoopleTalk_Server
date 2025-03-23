package com.demo.loopleTalk.dto.comment;

import lombok.Builder;

@Builder
public record CommentUpdateRequest(
	Long postId,
	String content
) {
}
