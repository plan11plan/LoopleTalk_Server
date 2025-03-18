package com.demo.loopleTalk.repository.post;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.loopleTalk.domain.post.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
	Optional<PostLike> findByMemberId(Long memberId);

	boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}
