package com.demo.loopleTalk.service.post;

import java.util.Comparator;
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
import com.demo.loopleTalk.dto.post.NearestPostResponse;
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
	private final PostCreateService postCreateService;

	@Transactional
	public void create(Long memberId, CreatePostRequest request) {
		postCreateService.create(memberId, request);
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

	@Transactional(readOnly = true)
	public CursorResponse<NearestPostResponse> getNearestPostsWithinScreen(
		Long memberId,
		double topRightX, double topRightY,
		double bottomLeftX, double bottomLeftY,
		double myX, double myY,
		double radiusKm,
		CursorRequest cursorRequest
	) {
		List<Post> allPosts = postRepository.findAll();

		// 바운딩 박스 조건: (longitude, latitude)가 [bottomLeftX, topRightX], [bottomLeftY, topRightY] 범위 내인지 필터
		List<Post> filteredByScreen = allPosts.stream()
			.filter(p -> p.getLongitude() >= bottomLeftX && p.getLongitude() <= topRightX)
			.filter(p -> p.getLatitude() >= bottomLeftY && p.getLatitude() <= topRightY)
			.collect(Collectors.toList());

		// 반경(radius) 필터: 내 위치 (myX, myY)로부터 radiusKm 이내
		List<PostDistance> filteredByRadius = filteredByScreen.stream()
			.map(p -> {
				double distance = calculateDistance(
					myY, myX,
					p.getLatitude(), p.getLongitude()
				);
				return new PostDistance(p, distance);
			})
			.filter(pd -> pd.distance <= radiusKm)
			.collect(Collectors.toList());

		// 커서 필터: cursorRequest.key()가 있다면 => postId < key
		List<PostDistance> afterCursorFilter = filteredByRadius.stream()
			.filter(pd -> {
				if (cursorRequest.hasKey()) {
					return pd.post.getPostId() < cursorRequest.key();
				}
				return true;
			})
			.collect(Collectors.toList());

		afterCursorFilter.sort(Comparator.comparingLong((PostDistance pd) -> pd.post.getPostId()).reversed());

		// size만큼만 잘라내기 -> (cursorRequest.size() 개까지만 보여주기)
		int limit = Math.min(afterCursorFilter.size(), cursorRequest.size());
		List<PostDistance> pagedResult = afterCursorFilter.subList(0, limit);

		// 다음 커서 계산: 가져온 목록 중 최솟값 postId
		long nextKey = pagedResult.stream()
			.mapToLong(pd -> pd.post.getPostId())
			.min()
			.orElse(CursorRequest.NONE_KEY);

		Member currentMember = getMember(memberId);
		List<NearestPostResponse> content = pagedResult.stream()
			.map(pd -> {
				Post post = pd.post;
				double distance = pd.distance;

				boolean liked = postLikeRepository.existsByMemberIdAndPostId(currentMember.getMemberId(),
					post.getPostId());
				List<PostHashtag> hashtags = postHashtagRepository.findAllByPost(post);
				String postImageUrl = s3Repository.getFileUrl(post.getPostId());
				boolean modifiable = currentMember.equals(post.getMember());

				Profile writerProfile = post.getMember().getProfile();
				SinglePostResponse postResponse = new SinglePostResponse(
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

				return new NearestPostResponse(postResponse, distance);
			})
			.collect(Collectors.toList());

		return new CursorResponse<>(
			cursorRequest.next(nextKey),
			content
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

	/**
	 * Haversine 공식 이용, 두 지점 사이의 거리(km)
	 */
	private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		final int R = 6371;
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
			+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
			* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c;
	}

	private record PostDistance(Post post, double distance) {
	}
}
