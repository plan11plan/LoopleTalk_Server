package com.demo.loopleTalk.controller.comment;

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

import com.demo.loopleTalk.dto.comment.CommentCreateRequest;
import com.demo.loopleTalk.dto.comment.CommentDeleteRequest;
import com.demo.loopleTalk.dto.comment.CommentGetByCursorRequest;
import com.demo.loopleTalk.dto.comment.CommentGetSingleRequest;
import com.demo.loopleTalk.dto.comment.CommentGetSingleResponse;
import com.demo.loopleTalk.dto.comment.CommentReplyReqeust;
import com.demo.loopleTalk.dto.comment.CommentResponse;
import com.demo.loopleTalk.dto.comment.CommentUpdateRequest;
import com.demo.loopleTalk.service.comment.CommentService;
import com.demo.loopleTalk.service.support.CursorRequest;
import com.demo.loopleTalk.service.support.CursorResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<CommentResponse> createComment(
		@RequestParam Long memberId,
		@RequestBody CommentCreateRequest createDto) {

		CommentResponse response = commentService.createComment(memberId, createDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/{parentId}")
	public ResponseEntity<CommentResponse> addReply(
		@RequestParam Long memberId,
		@PathVariable(value = "parentId") Long parentId,
		@RequestBody CommentReplyReqeust replyDto) {

		CommentResponse response = commentService.createReplyComment(memberId, parentId, replyDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{commentId}")
	public ResponseEntity<CommentGetSingleResponse> getComment(
		@RequestParam Long memberId,
		@RequestBody CommentGetSingleRequest commentGetSingleRequest,
		@PathVariable("commentId") Long commentId) {

		CommentGetSingleResponse response = commentService.getComment(memberId, commentGetSingleRequest, commentId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PatchMapping("/{commentId}")
	public ResponseEntity<CommentResponse> updateComment(
		@RequestParam Long memberId,
		@PathVariable Long commentId,
		@RequestBody CommentUpdateRequest updateDto) {

		CommentResponse response = commentService.updateComment(memberId, commentId, updateDto);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteComment(
		@RequestParam Long memberId,
		@RequestBody CommentDeleteRequest commentDeleteRequest,
		@PathVariable Long commentId) {

		commentService.deleteComment(memberId, commentDeleteRequest, commentId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	// 부모 + 댓글들 전부 포함
	@GetMapping()
	public ResponseEntity<CursorResponse<CommentGetSingleResponse>> getCommentsByCursor(
		@RequestParam Long memberId,
		@RequestBody CommentGetByCursorRequest commentGetByCursorRequest,
		@ModelAttribute CursorRequest cursorRequest
	) {

		CursorResponse<CommentGetSingleResponse> response = commentService.getCommentsByCursor(memberId,
			commentGetByCursorRequest, cursorRequest);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// 부모 댓글들만
	@GetMapping("/posts/{postId}")
	public ResponseEntity<CursorResponse<CommentGetSingleResponse>> getRootComments(
		@RequestParam Long memberId,
		@PathVariable Long postId,
		@ModelAttribute CursorRequest cursorRequest
	) {
		CursorResponse<CommentGetSingleResponse> response = commentService.getRootCommentsByCursor(memberId, postId,
			cursorRequest);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// 자식 댓글들만
	@GetMapping("/posts/{postId}/{parentId}/replies")
	public ResponseEntity<CursorResponse<CommentGetSingleResponse>> getReplies(
		@RequestParam Long memberId,
		@PathVariable Long parentId,
		@ModelAttribute CursorRequest cursorRequest
	) {
		CursorResponse<CommentGetSingleResponse> response = commentService.getRepliesByCursor(memberId, parentId,
			cursorRequest);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
