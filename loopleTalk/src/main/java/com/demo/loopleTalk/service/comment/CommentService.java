package com.demo.loopleTalk.service.comment;

import com.demo.loopleTalk.dto.comment.CommentCreateDto;
import com.demo.loopleTalk.dto.comment.CommentDeleteDto;
import com.demo.loopleTalk.dto.comment.CommentGetSingleDto;
import com.demo.loopleTalk.dto.comment.CommentResponseDto;
import com.demo.loopleTalk.dto.comment.CommentUpdateDto;

public interface CommentService {
	CommentResponseDto createComment(Long memberId, CommentCreateDto createDto);

	CommentResponseDto getComment(Long memberId, CommentGetSingleDto commentGetSingleDto, Long commentId);

	CommentResponseDto updateComment(Long memberId, Long commentId, CommentUpdateDto updateDto);

	void deleteComment(Long memberId, CommentDeleteDto commentDeleteDto, Long commentId);
}
