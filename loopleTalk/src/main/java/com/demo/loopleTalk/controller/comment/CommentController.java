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

import com.demo.loopleTalk.dto.comment.CommentCreateDto;
import com.demo.loopleTalk.dto.comment.CommentDeleteDto;
import com.demo.loopleTalk.dto.comment.CommentGetSingleDto;
import com.demo.loopleTalk.dto.comment.CommentResponseDto;
import com.demo.loopleTalk.dto.comment.CommentUpdateDto;
import com.demo.loopleTalk.service.comment.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<CommentResponseDto> createComment(
		@RequestParam Long memberId,
		@RequestBody CommentCreateDto createDto) {

		CommentResponseDto response = commentService.createComment(memberId, createDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{commentId}")
	public ResponseEntity<CommentResponseDto> getComment(
		@RequestParam Long memberId,
		@RequestBody CommentGetSingleDto commentGetSingleDto,
		@PathVariable("commentId") Long commentId) {

		CommentResponseDto response = commentService.getComment(memberId, commentGetSingleDto, commentId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{commentId}")
	public ResponseEntity<CommentResponseDto> updateComment(
		@RequestParam Long memberId,
		@PathVariable Long commentId,
		@RequestBody CommentUpdateDto updateDto) {

		CommentResponseDto response = commentService.updateComment(memberId, commentId, updateDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteComment(
		@RequestParam Long memberId,
		@RequestBody CommentDeleteDto commentDeleteDto,
		@PathVariable Long commentId) {

		commentService.deleteComment(memberId, commentDeleteDto, commentId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
