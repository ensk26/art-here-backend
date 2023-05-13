package com.backend.arthere.comment.domain;

import com.backend.arthere.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.backend.arthere.fixture.CommentFixtures.*;
import static com.backend.arthere.fixture.MemberFixtures.회원;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommentTest {
    
    @Test
    @DisplayName("댓글 내용을 수정한다.")
    public void 댓글_수정() throws Exception {
        //given
        Comment comment = Comment.builder()
                .content(댓글_내용)
                .build();
        
        //when
        comment.updateContent(댓글_내용_수정);
        
        //then
        assertThat(comment.getContent()).isEqualTo(댓글_내용_수정);
    }
    
    @Test
    @DisplayName("댓글 작성자인 경우 true 반환한다.")
    public void 댓글_작성자_확인() throws Exception {
        //given
        Member member = 회원();
        Comment comment = Comment.builder()
                .member(member)
                .content(댓글_내용)
                .build();

        //when //then
        assertThat(comment.isWriter(member)).isTrue();
    }
}