package com.demo.loopleTalk.dto.comment;

import lombok.Builder;

@Builder
public record CommentReplyReqeust(
	Long postId,
	String content
) {
}
