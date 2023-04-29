package com.backend.arthere.post.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join fetch p.member where p.id = :id")
    Optional<Post> findPostWithMember(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("update Post set likeCount = likeCount + 1 where id = :id")
    void increaseLikeCount(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("update Post set likeCount = likeCount - 1 where id = :id")
    void decreaseLikeCount(@Param("id") Long id);
}
