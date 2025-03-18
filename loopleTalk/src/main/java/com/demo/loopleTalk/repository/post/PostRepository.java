package com.demo.loopleTalk.repository.post;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.loopleTalk.domain.member.Member;
import com.demo.loopleTalk.domain.post.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	// 커서 없이 memberId만 주어졌을 때 (최신순 정렬) 하기 위해 만듬
	Optional<List<Post>> findAllByMember(Member member, Pageable pageable);

	// 커서(postId) 미만인 게시글만 조회 하기 위해 만듬
	Optional<List<Post>> findAllByMemberAndPostIdLessThan(Member member, Long postId, Pageable pageable);
}
