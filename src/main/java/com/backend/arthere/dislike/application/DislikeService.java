package com.backend.arthere.dislike.application;

import com.backend.arthere.dislike.domain.Dislike;
import com.backend.arthere.dislike.domain.DislikeRepository;
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
public class DislikeService {

    private final DislikeRepository dislikeRepository;

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public void addDislike(final Long postId, final Long memberId) {
        Post post = findPost(postId);
        Member member = findMember(memberId);
        saveDislike(post, member);
    }

    @Transactional
    public void subtractDislike(final Long postId, final Long memberId) {
        Post post = findPost(postId);
        Member member = findMember(memberId);
        deleteDislike(post.getId(), member.getId());
    }

    private void saveDislike(final Post post, final Member member) {
        if(!dislikeRepository.existsByMemberIdAndPostId(member.getId(), post.getId())) {
            Dislike dislike = new Dislike(member, post);
            dislikeRepository.save(dislike);
            postRepository.increaseDislikeCount(post.getId());
        }
    }

    private void deleteDislike(final Long postId, final Long memberId) {
        if(dislikeRepository.existsByMemberIdAndPostId(memberId, postId)) {
            postRepository.decreaseDislikeCount(postId);
            dislikeRepository.deleteByMemberIdAndPostId(memberId, postId);
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
