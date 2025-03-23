package com.demo.loopleTalk.dto.comment;

import lombok.Builder;

@Builder
public record CommentGetSingleResponse(
	String profileImage,
	String nickname,
	CommentResponse commentResponse
) {
}
