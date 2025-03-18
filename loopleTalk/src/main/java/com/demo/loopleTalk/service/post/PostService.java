package com.demo.loopleTalk.service.post;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.demo.loopleTalk.service.post.support.CursorRequest;
import com.demo.loopleTalk.service.post.support.CursorResponse;

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

	@Transactional
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

	@Transactional(readOnly = true)
	public SinglePostResponse getPost(Long memberId, Long postId) {
		Member requestMember = getMember(memberId);

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

		return toSinglePostResponse(post, requestMember);
	}

	@Transactional(readOnly = true)
	public CursorResponse<SinglePostResponse> getPostsByCursor(Long memberId, CursorRequest cursorRequest) {
		Member member = getMember(memberId);
		List<Post> posts = findAllBy(member, cursorRequest);

		// nextKey 계산 -> 가져온 Post들 중 가장 작은 postId (최소값)
		long nextKey = posts.stream()
			.mapToLong(Post::getPostId)
			.min()
			.orElse(CursorRequest.NONE_KEY); // 비어있으면 -1

		// Post -> SinglePostResponse 변환
		List<SinglePostResponse> contentDtos = posts.stream()
			.map(post -> {
				// DTO 변환 로직
				boolean modifiable = post.getMember().getMemberId().equals(member.getMemberId());
				boolean liked = postLikeRepository.existsByMemberIdAndPostId(member.getMemberId(),
					post.getPostId());
				List<PostHashtag> hashtags = postHashtagRepository.findAllByPost(post);
				String postImageUrl = s3Repository.getFileUrl(post.getPostId());

				Profile writerProfile = post.getMember().getProfile();
				return new SinglePostResponse(
					post.getPostId(),
					postImageUrl,
					writerProfile.getNickname(),
					writerProfile.getLocation(),
					writerProfile.isGender(),
					post.getContent(),
					post.getLikeCount(),
					post.getCommentCount(),
					hashtags,
					modifiable,
					liked,
					post.getCreatedAt()
				);
			})
			.collect(Collectors.toList());

		return new CursorResponse<>(
			cursorRequest.next(nextKey),
			contentDtos
		);
	}

	/**
	 * memberId 기준 + cursorRequest(커서) 기준으로 게시글 조회
	 *   - 커서가 있으면: "postId < cursorRequest.key()" 조건
	 *   - 커서가 없으면: 모든 게시글 중 최신부터 size 개
	 */
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

	private List<Post> findAllBy(Member member, CursorRequest cursorRequest) {
		// page=0, size=cursorRequest.size(), 정렬: postId DESC
		var pageable = PageRequest.of(0, cursorRequest.size(), Sort.by(Sort.Direction.DESC, "postId"));

		if (cursorRequest.hasKey()) {
			// 커서가 있으면 ->(postId < key)
			return postRepository.findAllByMemberAndPostIdLessThan(
					member,
					cursorRequest.key(),
					pageable
				)
				.orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
		} else {
			// 커서가 없으면 -> 전체(가장 최신부터)
			return postRepository.findAllByMember(
					member,
					pageable
				)
				.orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
		}
	}

	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
	}

}
