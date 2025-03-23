package com.demo.loopleTalk.service.comment;

import org.springframework.stereotype.Component;

import com.demo.loopleTalk.domain.comment.Comment;
import com.demo.loopleTalk.repository.comment.CommentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentReadComponent {

	private final CommentRepository commentRepository;

	public Comment getCommentById(Long id) {
		return commentRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));
	}
}
