package com.demo.loopleTalk.service.comment;

import com.demo.loopleTalk.dto.comment.CommentCreateRequest;
import com.demo.loopleTalk.dto.comment.CommentDeleteRequest;
import com.demo.loopleTalk.dto.comment.CommentGetByCursorRequest;
import com.demo.loopleTalk.dto.comment.CommentGetSingleRequest;
import com.demo.loopleTalk.dto.comment.CommentGetSingleResponse;
import com.demo.loopleTalk.dto.comment.CommentReplyReqeust;
import com.demo.loopleTalk.dto.comment.CommentResponse;
import com.demo.loopleTalk.dto.comment.CommentUpdateRequest;
import com.demo.loopleTalk.service.support.CursorRequest;
import com.demo.loopleTalk.service.support.CursorResponse;

public interface CommentService {
	CommentResponse createComment(Long memberId, CommentCreateRequest createDto);

	CommentResponse createReplyComment(Long memberId, Long parentId, CommentReplyReqeust request);

	CommentGetSingleResponse getComment(Long memberId, CommentGetSingleRequest commentGetSingleRequest, Long commentId);

	CommentResponse updateComment(Long memberId, Long commentId, CommentUpdateRequest updateDto);

	void deleteComment(Long memberId, CommentDeleteRequest commentDeleteRequest, Long commentId);

	CursorResponse<CommentGetSingleResponse> getCommentsByCursor(Long memberId,
		CommentGetByCursorRequest commentGetByCursorRequest,
		CursorRequest cursorRequest);

}
