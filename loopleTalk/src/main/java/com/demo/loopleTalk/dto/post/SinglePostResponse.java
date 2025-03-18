package com.demo.loopleTalk.dto.post;

import java.time.LocalDateTime;
import java.util.List;

import com.demo.loopleTalk.domain.post.PostHashtag;

public record SinglePostResponse(
	Long postId,
	String profileImage,
	String nickname,
	String memberLocation,
	boolean gender,
	String content,
	int likeCount,
	int commentCount,
	List<PostHashtag> postHashtags,

	boolean modifiable,
	boolean liked,
	LocalDateTime createdAt

) {
}
