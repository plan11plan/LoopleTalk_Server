package com.demo.loopleTalk.controller.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.loopleTalk.dto.post.CreatePostRequest;
import com.demo.loopleTalk.dto.post.NearestPostResponse;
import com.demo.loopleTalk.dto.post.SinglePostResponse;
import com.demo.loopleTalk.dto.post.UpdatePostRequest;
import com.demo.loopleTalk.service.post.PostService;
import com.demo.loopleTalk.service.support.CursorRequest;
import com.demo.loopleTalk.service.support.CursorResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {
	private final PostService postService;

	@PostMapping
	public ResponseEntity<Void> addPost(
		@RequestParam Long memberId,
		@Valid @RequestBody CreatePostRequest request) {

		postService.createPost(memberId, request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PatchMapping("/{postId}")
	public ResponseEntity<Void> updatePost(
		@RequestParam Long memberId,
		@PathVariable("postId") Long postId,
		@Valid @RequestBody UpdatePostRequest request) {

		postService.updatePost(memberId, postId, request);
		return ResponseEntity.status(HttpStatus.OK).build();

	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> deletePost(
		@RequestParam Long memberId,
		@PathVariable("postId") Long postId) {

		postService.deletePost(memberId, postId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/{postId}")
	public ResponseEntity<SinglePostResponse> getSinglePost(
		@RequestParam Long memberId,
		@PathVariable("postId") Long postId
	) {
		SinglePostResponse response = postService.getSinglePost(memberId, postId);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/cursor")
	public ResponseEntity<CursorResponse<SinglePostResponse>> getPostsByCursor(
		@RequestParam Long memberId,
		@ModelAttribute CursorRequest cursorRequest
	) {
		CursorResponse<SinglePostResponse> response = postService.getPostsByCursor(memberId, cursorRequest);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/cursor/nearest")
	public ResponseEntity<CursorResponse<NearestPostResponse>> getNearestPosts(
		@RequestParam Long memberId,
		@RequestParam double myX,
		@RequestParam double myY,
		@RequestParam(required = false, defaultValue = "10") Double radius,
		@ModelAttribute CursorRequest cursorRequest
	) {
		CursorResponse<NearestPostResponse> response = postService.getNearestPosts(
			memberId,
			myX, myY,
			radius,
			cursorRequest
		);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
