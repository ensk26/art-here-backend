package com.backend.arthere.like.domain;

import com.backend.arthere.member.domain.Member;
import com.backend.arthere.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByMemberAndPost(Member member, Post post);

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    void deleteByMemberIdAndPostId(Long memberId, Long postId);
}
