package com.demo.loopleTalk.controller.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.demo.loopleTalk.dto.post.CreatePostRequest;
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
	public void create(MockUserDetails mockUserDetails, @Valid @RequestBody CreatePostRequest request) {
		Long memberId = mockUserDetails.getMemberId();
		postService.create(memberId, request);
	}

	@GetMapping("/{postId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<SinglePostResponse> getSinglePost(
		MockUserDetails mockUserDetails,
		@PathVariable("postId") Long postId
	) {
		Long memberId = mockUserDetails.getMemberId();
		SinglePostResponse response = postService.getPost(memberId, postId);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/cursor")
	public ResponseEntity<CursorResponse<SinglePostResponse>> getPostsByCursor(
		MockUserDetails mockUserDetails,
		@ModelAttribute CursorRequest cursorRequest
	) {
		Long memberId = mockUserDetails.getMemberId();
		CursorResponse<SinglePostResponse> response = postService.getPostsByCursor(memberId, cursorRequest);

		return ResponseEntity.ok(response);
	}
}
