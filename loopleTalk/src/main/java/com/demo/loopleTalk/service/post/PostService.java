package com.demo.loopleTalk.service.post;

import java.util.List;

import org.springframework.stereotype.Service;

import com.demo.loopleTalk.domain.member.Member;
import com.demo.loopleTalk.domain.post.Post;
import com.demo.loopleTalk.domain.post.PostHashtag;
import com.demo.loopleTalk.domain.profile.Profile;
import com.demo.loopleTalk.dto.post.CreatePostRequest;
import com.demo.loopleTalk.dto.post.SinglePostResponse;
import com.demo.loopleTalk.repository.member.MemberRepository;
import com.demo.loopleTalk.repository.post.PostHashtagRepository;
import com.demo.loopleTalk.repository.post.PostLikeRepository;
import com.demo.loopleTalk.repository.post.PostRepository;
import com.demo.loopleTalk.repository.post.S3Repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final PostLikeRepository postLikeRepository;
	private final PostHashtagRepository postHashtagRepository;
	private final S3Repository s3Repository;

	public void create(Long memberId, CreatePostRequest request) {
		Member member = getMember(memberId);

		Post createdPost = Post.builder()
			.member(member)
			.content(request.content())
			.longitude(request.longitude())
			.latitude(request.latitude())
			.build();
		postRepository.save(createdPost);
	}

	public SinglePostResponse getPost(Long memberId, Long postId) {
		Member requestMember = getMember(memberId);

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

		return toSinglePostResponse(post, requestMember);
	}

	private SinglePostResponse toSinglePostResponse(Post post, Member requestMember) {
		Member writer = post.getMember();
		Profile writerProfile = writer.getProfile();

		// 좋아요 여부
		boolean liked = postLikeRepository.existsByMemberIdAndPostId(requestMember.getMemberId(), post.getPostId());
		// 해시태그 목록
		List<PostHashtag> postHashtags = postHashtagRepository.findAllByPost(post);

		// S3에서 이미지 URL 가져오기
		String postImageUrl = s3Repository.getFileUrl(post.getPostId());

		// 요청 보낸 멤버와 글 작성자 동일인 여부
		boolean modifiable = requestMember.equals(writer);

		return new SinglePostResponse(
			post.getPostId(),
			postImageUrl,
			writerProfile.getNickname(),
			writerProfile.getLocation(),
			writerProfile.isGender(),
			post.getContent(),
			post.getLikeCount(),
			post.getCommentCount(),
			postHashtags,
			modifiable,
			liked,
			post.getCreatedAt()
		);
	}

	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
	}

}
