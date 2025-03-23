package com.demo.loopleTalk.service.comment;

import org.springframework.stereotype.Component;

import com.demo.loopleTalk.domain.comment.Comment;
import com.demo.loopleTalk.dto.comment.CommentUpdateDto;
import com.demo.loopleTalk.repository.comment.CommentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentUpdateComponent {

	private final CommentRepository commentRepository;

	public Comment updateComment(Comment comment, CommentUpdateDto updateDto) {
		Comment updatedComment = comment.changeComment(updateDto.content());
		return commentRepository.save(updatedComment);
	}
}
