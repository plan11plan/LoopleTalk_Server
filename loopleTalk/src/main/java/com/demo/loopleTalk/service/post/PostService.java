package com.demo.loopleTalk.service.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.loopleTalk.dto.post.CreatePostRequest;
import com.demo.loopleTalk.dto.post.NearestPostResponse;
import com.demo.loopleTalk.dto.post.SinglePostResponse;
import com.demo.loopleTalk.dto.post.UpdatePostRequest;
import com.demo.loopleTalk.service.support.CursorRequest;
import com.demo.loopleTalk.service.support.CursorResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostCreateService postCreateService;
	private final PostUpdateService postUpdateService;
	private final PostDeleteService postDeleteService;
	private final PostGetSingleService postGetSingleService;
	private final PostGetByCursorService postGetByCursorService;
	private final PostGetNearestService postGetNearestService;

	public void deletePost(Long memberId, Long postId) {
		postDeleteService.deletePost(memberId, postId);
	}

	@Transactional
	public void createPost(Long memberId, CreatePostRequest request) {
		postCreateService.createPost(memberId, request);
	}

	@Transactional
	public void updatePost(Long memberId, Long postId, UpdatePostRequest request) {
		postUpdateService.updatePost(memberId, postId, request);
	}

	@Transactional(readOnly = true)
	public SinglePostResponse getSinglePost(Long memberId, Long postId) {
		return postGetSingleService.getSinglePost(memberId, postId);
	}

	@Transactional(readOnly = true)
	public CursorResponse<SinglePostResponse> getPostsByCursor(Long memberId, CursorRequest cursorRequest) {
		return postGetByCursorService.getPostsByCursor(memberId, cursorRequest);
	}

	@Transactional(readOnly = true)
	public CursorResponse<NearestPostResponse> getNearestPosts(
		Long memberId,
		double myX, double myY,
		double radiusKm,
		CursorRequest cursorRequest
	) {

		return postGetNearestService.getNearestPosts(memberId, myX, myY, radiusKm, cursorRequest);
	}

}
