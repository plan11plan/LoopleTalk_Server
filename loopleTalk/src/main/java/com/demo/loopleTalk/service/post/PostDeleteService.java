package com.demo.loopleTalk.service.post;

import org.springframework.stereotype.Service;

import com.demo.loopleTalk.domain.post.Post;
import com.demo.loopleTalk.repository.member.MemberRepository;
import com.demo.loopleTalk.repository.post.PostRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostDeleteService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;

	public void deletePost(Long memberId, Long postId) {
		validateMember(memberId);
		Post post = getPost(postId);
		postRepository.delete(post);
	}

	private void validateMember(Long memberId) {
		memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
	}

	private Post getPost(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
	}
}
