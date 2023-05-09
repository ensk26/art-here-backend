package com.backend.arthere.comment.application;

import com.backend.arthere.comment.domain.Comment;
import com.backend.arthere.comment.domain.CommentRepository;
import com.backend.arthere.comment.dto.request.CommentRequest;
import com.backend.arthere.comment.exception.CommentNotFoundException;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.member.exception.MemberNotFoundException;
import com.backend.arthere.member.exception.NotWriterException;
import com.backend.arthere.post.domain.Post;
import com.backend.arthere.post.domain.PostRepository;
import com.backend.arthere.post.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(final Long postId, final Long memberId, final CommentRequest commentRequest) {
        Post post = findPost(postId);
        Member member = findMember(memberId);
        Comment comment = commentRequest.toComment(post, member);

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public void update(final Long commentId, final Long memberId, final CommentRequest commentRequest) {
        Member member = findMember(memberId);
        Comment comment = findComment(commentId);
        validateWriter(member, comment);

        comment.updateContent(commentRequest.getContent());
    }

    private void validateWriter(final Member member, final Comment comment) {
        if(!comment.isWriter(member)) {
            throw new NotWriterException();
        }
    }

    private Comment findComment(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
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
