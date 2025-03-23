package com.demo.loopleTalk.service.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.loopleTalk.domain.post.Post;
import com.demo.loopleTalk.dto.post.UpdatePostRequest;
import com.demo.loopleTalk.repository.member.MemberRepository;
import com.demo.loopleTalk.repository.post.PostRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostUpdateService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public void updatePost(Long memberId, Long postId, UpdatePostRequest request) {
		validateMember(memberId);

		String toContent = request.content();

		Post post = getPost(postId);
		post.changeContent(toContent);
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
