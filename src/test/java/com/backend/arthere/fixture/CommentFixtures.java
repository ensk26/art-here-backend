package com.backend.arthere.fixture;

import com.backend.arthere.comment.domain.Comment;
import com.backend.arthere.comment.dto.response.CommentPageResponse;
import com.backend.arthere.comment.dto.response.CommentResponse;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.dto.response.WriterResponse;
import com.backend.arthere.post.domain.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.backend.arthere.fixture.MemberFixtures.회원;
import static com.backend.arthere.fixture.MemberFixtures.회원_아이디;

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

    public static Comment 댓글(final Long id, final Member member) {
        return Comment.builder()
                .id(id)
                .content(댓글_내용)
                .member(member).build();
    }

    public static CommentPageResponse 댓글_목록_응답() {
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (int i = 3; i >= 1; i--) {
            CommentResponse commentResponse = new CommentResponse(Long.valueOf(i), 댓글_내용, LocalDateTime.now(),
                    LocalDateTime.now(), true, WriterResponse.from(회원(회원_아이디)));
            commentResponses.add(commentResponse);
        }
        return new CommentPageResponse(false, commentResponses);
    }
}
