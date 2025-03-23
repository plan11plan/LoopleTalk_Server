package com.demo.loopleTalk.controller.comment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.loopleTalk.dto.comment.CommentCreateRequest;
import com.demo.loopleTalk.dto.comment.CommentDeleteRequest;
import com.demo.loopleTalk.dto.comment.CommentGetSingleRequest;
import com.demo.loopleTalk.dto.comment.CommentGetSingleResponse;
import com.demo.loopleTalk.dto.comment.CommentResponse;
import com.demo.loopleTalk.dto.comment.CommentUpdateRequest;
import com.demo.loopleTalk.service.comment.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<CommentResponse> createComment(
		@RequestParam Long memberId,
		@RequestBody CommentCreateRequest createDto) {

		CommentResponse response = commentService.createComment(memberId, createDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{commentId}")
	public ResponseEntity<CommentGetSingleResponse> getComment(
		@RequestParam Long memberId,
		@RequestBody CommentGetSingleRequest commentGetSingleRequest,
		@PathVariable("commentId") Long commentId) {

		CommentGetSingleResponse response = commentService.getComment(memberId, commentGetSingleRequest, commentId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{commentId}")
	public ResponseEntity<CommentResponse> updateComment(
		@RequestParam Long memberId,
		@PathVariable Long commentId,
		@RequestBody CommentUpdateRequest updateDto) {

		CommentResponse response = commentService.updateComment(memberId, commentId, updateDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteComment(
		@RequestParam Long memberId,
		@RequestBody CommentDeleteRequest commentDeleteRequest,
		@PathVariable Long commentId) {

		commentService.deleteComment(memberId, commentDeleteRequest, commentId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
