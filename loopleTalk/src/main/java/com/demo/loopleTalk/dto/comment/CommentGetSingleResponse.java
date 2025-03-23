package com.demo.loopleTalk.dto.comment;

public record CommentGetSingleResponse(
	String profileImage,
	String nickname,
	CommentResponse commentResponse
) {
}
