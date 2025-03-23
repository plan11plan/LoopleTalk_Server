package com.demo.loopleTalk.controller.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.demo.loopleTalk.dto.post.CreatePostRequest;
import com.demo.loopleTalk.dto.post.NearestPostResponse;
import com.demo.loopleTalk.dto.post.SinglePostResponse;
import com.demo.loopleTalk.service.post.PostService;
import com.demo.loopleTalk.service.post.support.CursorRequest;
import com.demo.loopleTalk.service.post.support.CursorResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {
	private final PostService postService;

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void create(@RequestParam Long memberId, @Valid @RequestBody CreatePostRequest request) {
		postService.create(memberId, request);
	}

	@GetMapping("/{postId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<SinglePostResponse> getSinglePost(
		@RequestParam Long memberId,
		@PathVariable("postId") Long postId
	) {
		SinglePostResponse response = postService.getPost(memberId, postId);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/cursor")
	public ResponseEntity<CursorResponse<SinglePostResponse>> getPostsByCursor(
		@RequestParam Long memberId,
		@ModelAttribute CursorRequest cursorRequest
	) {
		CursorResponse<SinglePostResponse> response = postService.getPostsByCursor(memberId, cursorRequest);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/nearest")
	public ResponseEntity<CursorResponse<NearestPostResponse>> getNearestPostsWithinScreen(
		@RequestParam Long memberId,
		@RequestParam double topRightX,
		@RequestParam double topRightY,
		@RequestParam double bottomLeftX,
		@RequestParam double bottomLeftY,
		@RequestParam double myX,
		@RequestParam double myY,
		@RequestParam(required = false, defaultValue = "10") Double radius,
		@ModelAttribute CursorRequest cursorRequest
	) {
		CursorResponse<NearestPostResponse> response = postService.getNearestPostsWithinScreen(
			memberId,
			topRightX, topRightY,
			bottomLeftX, bottomLeftY,
			myX, myY,
			radius,
			cursorRequest
		);

		return ResponseEntity.ok(response);
	}
}
