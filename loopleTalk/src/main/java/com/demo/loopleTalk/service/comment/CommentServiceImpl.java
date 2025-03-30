package com.demo.loopleTalk.service.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.demo.loopleTalk.domain.comment.Comment;
import com.demo.loopleTalk.domain.member.Member;
import com.demo.loopleTalk.domain.post.Post;
import com.demo.loopleTalk.dto.comment.CommentCreateRequest;
import com.demo.loopleTalk.dto.comment.CommentDeleteRequest;
import com.demo.loopleTalk.dto.comment.CommentGetByCursorRequest;
import com.demo.loopleTalk.dto.comment.CommentGetSingleRequest;
import com.demo.loopleTalk.dto.comment.CommentGetSingleResponse;
import com.demo.loopleTalk.dto.comment.CommentReplyReqeust;
import com.demo.loopleTalk.dto.comment.CommentResponse;
import com.demo.loopleTalk.dto.comment.CommentUpdateRequest;
import com.demo.loopleTalk.repository.member.MemberRepository;
import com.demo.loopleTalk.repository.post.PostRepository;
import com.demo.loopleTalk.repository.post.S3Repository;
import com.demo.loopleTalk.service.support.CursorRequest;
import com.demo.loopleTalk.service.support.CursorResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentAddComponent commentAddComponent;
	private final CommentGetSingleComponent commentGetSingleComponent;
	private final CommentGetByCursorComponent commentGetByCursorComponent;
	private final CommentUpdateComponent commentUpdateComponent;
	private final CommentDeleteComponent commentDeleteComponent;
	private final CommentGetRepliesByCursorComponent commentGetRepliesByCursorComponent;
	private final CommentGetRootCommentsByCursorComponent commentGetRootCommentsByCursorComponent;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final S3Repository s3Repository;

	@Override
	public CommentResponse createComment(Long memberId, CommentCreateRequest request) {
		Member member = getMember(memberId);
		Post post = validatePost(request.postId());
		Comment comment = commentAddComponent.addComment(member, post, request);
		return mapToResponseDto(comment);
	}

	@Override
	public CommentResponse createReplyComment(Long memberId, Long parentId, CommentReplyReqeust request) {
		Member member = getMember(memberId);
		Post post = validatePost(request.postId());
		Comment comment = commentAddComponent.addReply(member, post, parentId, request);
		return mapToResponseDto(comment);
	}

	@Override
	public CommentGetSingleResponse getComment(Long memberId, CommentGetSingleRequest commentGetSingleRequest,
		Long commentId) {
		Long postId = commentGetSingleRequest.postId();

		Member member = getMember(memberId);
		String nickname = member.getProfile().getNickname();
		String fileUrl = s3Repository.getFileUrl(commentId);
		validatePost(postId);
		Comment comment = commentGetSingleComponent.getCommentById(commentId);
		return new CommentGetSingleResponse(
			fileUrl,
			nickname,
			mapToResponseDto(comment));
	}

	@Override
	public CursorResponse<CommentGetSingleResponse> getCommentsByCursor(Long memberId,
		CommentGetByCursorRequest commentGetByCursorRequest,
		CursorRequest cursorRequest) {
		validateMember(memberId);

		Post post = validatePost(commentGetByCursorRequest.postId());
		CursorResponse<Comment> originalResponse = commentGetByCursorComponent.getCommentsByCursor(
			post,
			cursorRequest
		);

		List<CommentGetSingleResponse> mappedList = originalResponse.contents().stream()
			.map(comment -> {
				String nickname = comment.getMember().getProfile().getNickname();
				String profileImage = s3Repository.getFileUrl(comment.getMember().getMemberId());
				CommentResponse commentResponse = mapToResponseDto(comment);
				return CommentGetSingleResponse.builder()
					.nickname(nickname)
					.profileImage(profileImage)
					.commentResponse(commentResponse)
					.build();
			})
			.collect(Collectors.toList());

		return new CursorResponse<>(originalResponse.nextCursorRequest(), mappedList);
	}

	@Override
	public CursorResponse<CommentGetSingleResponse> getRootCommentsByCursor(Long memberId, Long postId,
		CursorRequest cursorRequest) {
		validateMember(memberId);
		Post post = validatePost(postId);

		// 최상위 댓글 컴포넌트를 이용하여 댓글 조회
		CursorResponse<Comment> originalResponse = commentGetRootCommentsByCursorComponent.getRootCommentsByCursor(post,
			cursorRequest);

		List<CommentGetSingleResponse> mappedList = originalResponse.contents().stream()
			.map(comment -> {
				String nickname = comment.getMember().getProfile().getNickname();
				String profileImage = s3Repository.getFileUrl(comment.getMember().getMemberId());
				// CommentResponse.withReplyCount() 정적 팩토리 메서드를 사용하여 댓글 응답 생성
				int replyCount = comment.getReplies() != null ? comment.getReplies().size() : 0;
				CommentResponse commentResponse = CommentResponse.withReplyCount(comment, replyCount);
				return CommentGetSingleResponse.builder()
					.nickname(nickname)
					.profileImage(profileImage)
					.commentResponse(commentResponse)
					.build();
			})
			.collect(Collectors.toList());

		return new CursorResponse<>(originalResponse.nextCursorRequest(), mappedList);
	}

	@Override
	public CursorResponse<CommentGetSingleResponse> getRepliesByCursor(Long memberId, Long parentId,
		CursorRequest cursorRequest) {
		validateMember(memberId);
		// 부모 댓글을 먼저 조회
		Comment parent = commentGetSingleComponent.getCommentById(parentId);
		validatePost(parent.getPost().getPostId());

		// 대댓글 컴포넌트를 이용하여 자식 댓글 조회
		CursorResponse<Comment> originalResponse = commentGetRepliesByCursorComponent.getRepliesByCursor(parent,
			cursorRequest);

		List<CommentGetSingleResponse> mappedList = originalResponse.contents().stream()
			.map(comment -> {
				String nickname = comment.getMember().getProfile().getNickname();
				String profileImage = s3Repository.getFileUrl(comment.getMember().getMemberId());
				// 단순 매핑은 기본 팩토리 메서드 from() 사용 (대댓글은 replyCount 필요 없을 경우)
				CommentResponse commentResponse = CommentResponse.from(comment);
				return CommentGetSingleResponse.builder()
					.nickname(nickname)
					.profileImage(profileImage)
					.commentResponse(commentResponse)
					.build();
			})
			.collect(Collectors.toList());

		return new CursorResponse<>(originalResponse.nextCursorRequest(), mappedList);
	}

	@Override
	public CommentResponse updateComment(Long memberId, Long commentId, CommentUpdateRequest request) {
		validateMember(memberId);
		validatePost(request.postId());
		Comment comment = commentGetSingleComponent.getCommentById(commentId);
		Comment updatedComment = commentUpdateComponent.updateComment(comment, request);
		return mapToResponseDto(updatedComment);
	}

	@Override
	public void deleteComment(Long memberId, CommentDeleteRequest commentDeleteRequest, Long commentId) {
		validateMember(memberId);
		Long postId = commentDeleteRequest.postId();
		validatePost(postId);

		Comment comment = commentGetSingleComponent.getCommentById(commentId);
		commentDeleteComponent.deleteComment(comment);
	}

	private CommentResponse mapToResponseDto(Comment comment) {
		return CommentResponse.builder()
			.id(comment.getId())
			.postId(comment.getPost().getPostId())
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.updatedAt(comment.getUpdatedAt())
			.build();
	}

	private void validateMember(Long memberId) {
		memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
	}

	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
	}

	private Post validatePost(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
	}

}
