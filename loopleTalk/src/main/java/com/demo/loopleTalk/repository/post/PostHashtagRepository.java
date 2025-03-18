package com.demo.loopleTalk.repository.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.loopleTalk.domain.post.Post;
import com.demo.loopleTalk.domain.post.PostHashtag;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
	List<PostHashtag> findAllByPost(Post post);

}
