package com.demo.loopleTalk.service.comment;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.demo.loopleTalk.domain.comment.Comment;
import com.demo.loopleTalk.domain.member.Member;
import com.demo.loopleTalk.domain.post.Post;
import com.demo.loopleTalk.dto.comment.CommentCreateRequest;
import com.demo.loopleTalk.dto.comment.CommentReplyReqeust;
import com.demo.loopleTalk.repository.comment.CommentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentAddComponent {

	private final CommentRepository commentRepository;
	private final CommentGetSingleComponent commentGetSingleComponent;

	@Transactional
	public Comment addComment(Member member, Post post, CommentCreateRequest request) {
		String content = request.content();
		Comment comment = Comment.addComment(member, content, post);
		return commentRepository.save(comment);
	}

	@Transactional
	public Comment addReply(Member member, Post post, Long parentId, CommentReplyReqeust request) {
		Comment parent = commentGetSingleComponent.getCommentById(parentId);
		String content = request.content();
		Comment reply = parent.addReply(member, post, content);
		return commentRepository.save(reply);
	}

}


