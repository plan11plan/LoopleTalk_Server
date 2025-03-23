package com.demo.loopleTalk.service.comment;

import org.springframework.stereotype.Component;

import com.demo.loopleTalk.domain.comment.Comment;
import com.demo.loopleTalk.repository.comment.CommentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentDeleteComponent {

	private final CommentRepository commentRepository;

	public void deleteComment(Comment comment) {

		commentRepository.delete(comment);
	}

}
