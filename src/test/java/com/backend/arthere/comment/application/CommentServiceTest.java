package com.backend.arthere.comment.application;

import com.backend.arthere.comment.domain.Comment;
import com.backend.arthere.comment.domain.CommentRepository;
import com.backend.arthere.comment.dto.request.CommentRequest;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.post.domain.Post;
import com.backend.arthere.post.domain.PostRepository;
import com.backend.arthere.post.exception.PostNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.backend.arthere.fixture.CommentFixtures.*;
import static com.backend.arthere.fixture.MemberFixtures.회원;
import static com.backend.arthere.fixture.MemberFixtures.회원_아이디;
import static com.backend.arthere.fixture.PostFixtures.게시물;
import static com.backend.arthere.fixture.PostFixtures.게시물_아이디;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("댓글을 작성한다.")
    public void 댓글_작성() throws Exception {
        //given
        Member member = 회원(회원_아이디);
        Post post = 게시물(게시물_아이디, member);
        CommentRequest commentRequest = new CommentRequest(댓글_내용);
        Comment comment = 댓글(댓글_아이디, member, post);

        given(postRepository.findById(post.getId()))
                .willReturn(Optional.of(post));
        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));

        given(commentRepository.save(any()))
                .willReturn(comment);
        //when
        commentService.save(post.getId(), member.getId(), commentRequest);

        //then
        assertAll(
                () -> verify(postRepository).findById(post.getId()),
                () -> verify(memberRepository).findById(member.getId())
        );
    }

    @Test
    @DisplayName("댓글을 작성할 게시물이 존재하지 않을 때 예외가 발생한다.")
    public void 게시물이_존재하지_않을_떄_예외_발생() throws Exception {
        //given
        CommentRequest commentRequest = new CommentRequest(댓글_내용);
        given(postRepository.findById(게시물_아이디))
                .willThrow(PostNotFoundException.class);
        //when //then
        assertThatThrownBy(() -> commentService.save(게시물_아이디, 회원_아이디, commentRequest))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("댓글을 수정한다.")
    public void 댓글_수정() throws Exception {
        //given
        CommentRequest commentRequest = new CommentRequest(댓글_내용_수정);
        Member member = 회원(회원_아이디);
        Comment comment = 댓글(댓글_아이디, member, null);

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));
        given(commentRepository.findById(comment.getId()))
                .willReturn(Optional.of(comment));
        //when
        commentService.update(comment.getId(), member.getId(), commentRequest);
        //then
        assertAll(
                () -> verify(commentRepository).findById(comment.getId()),
                () -> verify(memberRepository).findById(member.getId()),
                () -> assertThat(comment.getContent()).isEqualTo(commentRequest.getContent())
        );
    }
}