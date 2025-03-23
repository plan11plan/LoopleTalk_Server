package com.demo.loopleTalk.service.comment;

import org.springframework.stereotype.Component;

import com.demo.loopleTalk.domain.comment.Comment;
import com.demo.loopleTalk.domain.member.Member;
import com.demo.loopleTalk.domain.post.Post;
import com.demo.loopleTalk.dto.comment.CommentCreateDto;
import com.demo.loopleTalk.repository.comment.CommentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentAddComponent {

	private final CommentRepository commentRepository;

	public Comment addComment(Member member, Post post, CommentCreateDto request) {
		String content = request.content();
		Comment comment = Comment.addComment(member, content, post);
		return commentRepository.save(comment);
	}
}


