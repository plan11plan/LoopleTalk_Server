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
import com.demo.loopleTalk.dto.post.NearestPostResponse;
import com.demo.loopleTalk.dto.post.SinglePostResponse;
import com.demo.loopleTalk.repository.member.MemberRepository;
import com.demo.loopleTalk.repository.post.PostHashtagRepository;
import com.demo.loopleTalk.repository.post.PostLikeRepository;
import com.demo.loopleTalk.repository.post.PostRepository;
import com.demo.loopleTalk.repository.post.S3Repository;
import com.demo.loopleTalk.service.support.CursorRequest;
import com.demo.loopleTalk.service.support.CursorResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostGetNearestService {
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final PostLikeRepository postLikeRepository;
	private final PostHashtagRepository postHashtagRepository;
	private final S3Repository s3Repository;

	@Transactional(readOnly = true)
	public CursorResponse<NearestPostResponse> getNearestPosts(
		Long memberId,
		double myX, double myY,
		double radiusKm,
		CursorRequest cursorRequest
	) {
		// 1. 현재 회원 조회
		Member currentMember = getMember(memberId);

		// 2. 커서 기반으로 회원의 포스트 조회
		List<Post> posts = findAllBy(currentMember, cursorRequest);

		// 3. 각 포스트에 대해 내 좌표와의 거리 계산 후, 반경 내(post.distance <= radiusKm)인 포스트만 필터링
		List<PostDistance> postsWithinRadius = posts.stream()
			.map(p -> new PostDistance(p, calculateDistance(myX, myY, p.getLongitude(), p.getLatitude())))
			.filter(pd -> pd.distance <= radiusKm)
			.collect(Collectors.toList());

		// 4. 내 좌표와 가까운 순(거리 오름차순)으로 정렬
		postsWithinRadius.sort(Comparator.comparingDouble(pd -> pd.distance));

		// 5. 다음 커서 계산: repository 조회 시 사용한 커서 기준은 postId
		long nextKey = posts.isEmpty() ? CursorRequest.NONE_KEY
			: posts.stream().mapToLong(Post::getPostId).min().orElse(CursorRequest.NONE_KEY);

		// 6. 최종 응답 데이터 매핑
		List<NearestPostResponse> content = postsWithinRadius.stream()
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

		return new CursorResponse<>(cursorRequest.next(nextKey), content);
	}

	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
	}

	private double calculateDistance(double lon1, double lat1, double lon2, double lat2) {
		final int R = 6371; // 지구 반경(km)
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
			+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
			* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c;
	}

	/**
	 * 커서 기반 조회를 위한 회원의 포스트 조회
	 */
	private List<Post> findAllBy(Member member, CursorRequest cursorRequest) {
		// 페이지: 0, size=cursorRequest.size(), 정렬: postId DESC
		var pageable = PageRequest.of(0, cursorRequest.size(), Sort.by(Sort.Direction.DESC, "postId"));

		if (cursorRequest.hasKey()) {
			// 커서가 있으면 postId < key 조건 적용
			return postRepository.findAllByMemberAndPostIdLessThan(
					member,
					cursorRequest.key(),
					pageable
				)
				.orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
		} else {
			// 커서가 없으면 전체 조회(최신 순)
			return postRepository.findAllByMember(
					member,
					pageable
				)
				.orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
		}
	}

	private record PostDistance(Post post, double distance) {
	}
}
