package com.demo.loopleTalk.dto.comment;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CommentResponseDto(
	Long id,
	Long postId,
	String content,
	LocalDateTime createdAt,
	LocalDateTime updatedAt) {
}
