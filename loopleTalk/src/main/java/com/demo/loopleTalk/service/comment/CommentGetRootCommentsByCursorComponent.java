package com.demo.loopleTalk.service.comment;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.demo.loopleTalk.domain.comment.Comment;
import com.demo.loopleTalk.domain.post.Post;
import com.demo.loopleTalk.repository.comment.CommentRepository;
import com.demo.loopleTalk.service.support.CursorRequest;
import com.demo.loopleTalk.service.support.CursorResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentGetRootCommentsByCursorComponent {
	private final CommentRepository commentRepository;

	public CursorResponse<Comment> getRootCommentsByCursor(Post post, CursorRequest cursorRequest) {
		Pageable pageable = PageRequest.of(0, cursorRequest.size(), Sort.by(Sort.Direction.DESC, "id"));
		List<Comment> comments;
		if (cursorRequest.hasKey()) {
			comments = commentRepository.findAllByPostAndParentIsNullAndIdLessThan(post, cursorRequest.key(), pageable)
				.orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
		} else {
			comments = commentRepository.findAllByPostAndParentIsNull(post, pageable)
				.orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
		}

		long nextKey = comments.stream()
			.mapToLong(Comment::getId)
			.min()
			.orElse(CursorRequest.NONE_KEY);

		return new CursorResponse<>(cursorRequest.next(nextKey), comments);
	}
}
