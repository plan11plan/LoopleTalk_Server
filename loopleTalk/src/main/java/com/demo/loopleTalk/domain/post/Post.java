package com.demo.loopleTalk.domain.post;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.demo.loopleTalk.domain.member.Member;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id", updatable = false)
	private Long postId;

	@ManyToOne
	@Null
	@JoinColumn(name = "member_id")
	private Member member;

	@Nullable
	@Column(name = "content")
	private String content;

	@Nullable
	@Column(name = "longitude")

	private double longitude;

	@Nullable
	@Column(name = "latitude")
	private double latitude;

	@Column(name = "comment_count")
	private int commentCount;

	@Column(name = "like_count")
	private int likeCount;

	@CreatedDate
	@Nullable
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Nullable
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Builder
	public Post(Long postId, Member member, String content, double longitude, double latitude) {
		this.postId = postId;
		this.member = member;
		this.content = content;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public void updateCommentCount(int count) {
		this.commentCount += count;
	}

	public void updateLikeCount(int likeCount) {
		this.commentCount += likeCount;
	}
}
