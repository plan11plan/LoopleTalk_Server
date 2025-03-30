package com.demo.loopleTalk.dto.comment;

import java.time.LocalDateTime;

import com.demo.loopleTalk.domain.comment.Comment;

import lombok.Builder;

@Builder
public record CommentResponse(
	Long id,
	Long postId,
	String content,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	Integer replyCount
) {
	public static CommentResponse from(Comment comment) {
		return CommentResponse.builder()
			.id(comment.getId())
			.postId(comment.getPost().getPostId())
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.updatedAt(comment.getUpdatedAt())
			.replyCount(0)
			.build();
	}

	public static CommentResponse withReplyCount(Comment comment, int replyCount) {
		return CommentResponse.builder()
			.id(comment.getId())
			.postId(comment.getPost().getPostId())
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.updatedAt(comment.getUpdatedAt())
			.replyCount(replyCount)
			.build();
	}
}
