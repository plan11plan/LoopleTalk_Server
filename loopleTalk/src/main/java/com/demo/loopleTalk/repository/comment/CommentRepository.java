package com.demo.loopleTalk.repository.comment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.loopleTalk.domain.comment.Comment;
import com.demo.loopleTalk.domain.post.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	Optional<List<Comment>> findAllByPostAndIdLessThan(Post post, Long key, PageRequest pageable);

	Optional<List<Comment>> findAllByPost(Post post, PageRequest pageable);

}
