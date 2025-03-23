package com.demo.loopleTalk.service.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.loopleTalk.domain.member.Member;
import com.demo.loopleTalk.domain.post.Post;
import com.demo.loopleTalk.dto.post.CreatePostRequest;
import com.demo.loopleTalk.repository.member.MemberRepository;
import com.demo.loopleTalk.repository.post.PostRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCreateService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public void createPost(Long memberId, CreatePostRequest request) {
		Member member = getMember(memberId);

		Post createdPost = Post.builder()
			.member(member)
			.content(request.content())
			.longitude(request.longitude())
			.latitude(request.latitude())
			.build();
		postRepository.save(createdPost);
	}

	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
	}

}
