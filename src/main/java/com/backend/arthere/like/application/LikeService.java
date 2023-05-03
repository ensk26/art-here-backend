package com.backend.arthere.like.application;

import com.backend.arthere.like.domain.Like;
import com.backend.arthere.like.domain.LikeRepository;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.member.exception.MemberNotFoundException;
import com.backend.arthere.post.domain.Post;
import com.backend.arthere.post.domain.PostRepository;
import com.backend.arthere.post.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public void addLike(final Long postId, final Long memberId) {
        Post post = findPost(postId);
        Member member = findMember(memberId);
        saveLike(post, member);
    }

    @Transactional
    public void subtractLike(final Long postId, final Long memberId) {
        Post post = findPost(postId);
        Member member = findMember(memberId);
        deleteLike(post.getId(), member.getId());
    }

    private void saveLike(final Post post, final Member member) {

        if(!likeRepository.existsByMemberIdAndPostId(member.getId(), post.getId())) {
            Like like = new Like(member, post);
            likeRepository.save(like);
            postRepository.increaseLikeCount(post.getId());
        }
    }

    private void deleteLike(final Long postId, final Long memberId) {
        if(likeRepository.existsByMemberIdAndPostId(memberId, postId)) {
            postRepository.decreaseLikeCount(postId);
            likeRepository.deleteByMemberIdAndPostId(memberId, postId);
        }
    }


    private Post findPost(final Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

}
