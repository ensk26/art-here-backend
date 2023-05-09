package com.backend.arthere.fixture;

import com.backend.arthere.comment.domain.Comment;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.post.domain.Post;

public class CommentFixtures {

    public static final Long 댓글_아이디 = 1L;

    public static final String 댓글_내용 = "댓글 내용";

    public static final String 댓글_내용_수정 = "댓글 내용 수정";
    public static Comment 댓글(final Long id, final Member member, final Post post) {
        return Comment.builder()
                .id(id)
                .content(댓글_내용)
                .member(member)
                .post(post).build();
    }
}
