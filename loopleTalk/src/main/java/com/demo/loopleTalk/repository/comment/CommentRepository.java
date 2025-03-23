package com.demo.loopleTalk.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.loopleTalk.domain.comment.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	// 추가적인 쿼리 메서드가 필요하면 여기에 정의
}
