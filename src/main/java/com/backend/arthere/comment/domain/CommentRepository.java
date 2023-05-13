package com.backend.arthere.comment.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.member where c.post.id = :postId")
    Slice<Comment> findCommentByPostId(@Param("postId") Long postId, Pageable pageable);
}
