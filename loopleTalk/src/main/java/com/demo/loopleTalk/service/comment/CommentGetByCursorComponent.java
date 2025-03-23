package com.demo.loopleTalk.service.comment;

import java.util.List;

import org.springframework.data.domain.PageRequest;
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
public class CommentGetByCursorComponent {
	private final CommentRepository commentRepository;

	public CursorResponse getCommentsByCursor(Post post, CursorRequest cursorRequest) {
		List<Comment> comments = findAllBy(post, cursorRequest);

		// 다음 요청을 위한 nextKey: 조회된 댓글 중 가장 작은 id
		long nextKey = comments.stream()
			.mapToLong(Comment::getId)
			.min()
			.orElse(CursorRequest.NONE_KEY);

		return new CursorResponse<>(cursorRequest.next(nextKey), comments);
	}

	private List<Comment> findAllBy(Post post, CursorRequest cursorRequest) {
		// page=0, size=cursorRequest.size(), 정렬: id 내림차순 (최신 댓글부터)
		var pageable = PageRequest.of(0, cursorRequest.size(), Sort.by(Sort.Direction.DESC, "id"));

		if (cursorRequest.hasKey()) {
			// 커서가 있으면 -> id가 key보다 작은 댓글들 조회
			return commentRepository.findAllByPostAndIdLessThan(
					post,
					cursorRequest.key(),
					pageable
				)
				.orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
		} else {
			// 커서가 없으면 -> 해당 게시글의 전체 댓글 중 최신 댓글부터 조회
			return commentRepository.findAllByPost(
					post,
					pageable
				)
				.orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
		}
	}
}
