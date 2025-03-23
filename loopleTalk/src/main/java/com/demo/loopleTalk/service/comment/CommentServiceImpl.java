package com.demo.loopleTalk.service.comment;

import org.springframework.stereotype.Service;

import com.demo.loopleTalk.domain.comment.Comment;
import com.demo.loopleTalk.domain.member.Member;
import com.demo.loopleTalk.domain.post.Post;
import com.demo.loopleTalk.dto.comment.CommentCreateDto;
import com.demo.loopleTalk.dto.comment.CommentDeleteDto;
import com.demo.loopleTalk.dto.comment.CommentGetSingleDto;
import com.demo.loopleTalk.dto.comment.CommentResponseDto;
import com.demo.loopleTalk.dto.comment.CommentUpdateDto;
import com.demo.loopleTalk.repository.member.MemberRepository;
import com.demo.loopleTalk.repository.post.PostRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentAddComponent commentAddComponent;
	private final CommentReadComponent commentReadComponent;
	private final CommentUpdateComponent commentUpdateComponent;
	private final CommentDeleteComponent commentDeleteComponent;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;

	@Override
	public CommentResponseDto createComment(Long memberId, CommentCreateDto request) {
		Member member = getMember(memberId);
		Post post = validatePost(request.postId());
		Comment comment = commentAddComponent.addComment(member, post, request);
		return mapToResponseDto(comment);
	}

	@Override
	public CommentResponseDto getComment(Long memberId, CommentGetSingleDto commentGetSingleDto, Long commentId) {
		Long postId = commentGetSingleDto.postId();

		validateMember(memberId);
		validatePost(postId);
		Comment comment = commentReadComponent.getCommentById(commentId);
		return mapToResponseDto(comment);
	}

	@Override
	public CommentResponseDto updateComment(Long memberId, Long commentId, CommentUpdateDto request) {
		validateMember(memberId);
		validatePost(request.postId());
		Comment comment = commentReadComponent.getCommentById(commentId);
		Comment updatedComment = commentUpdateComponent.updateComment(comment, request);
		return mapToResponseDto(updatedComment);
	}

	@Override
	public void deleteComment(Long memberId, CommentDeleteDto commentDeleteDto, Long commentId) {
		validateMember(memberId);
		Long postId = commentDeleteDto.postId();
		validatePost(postId);

		Comment comment = commentReadComponent.getCommentById(commentId);
		commentDeleteComponent.deleteComment(comment);
	}

	private CommentResponseDto mapToResponseDto(Comment comment) {
		return CommentResponseDto.builder()
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
