package com.backend.arthere.comment.dto.request;

import com.backend.arthere.comment.domain.Comment;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.post.domain.Post;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentRequest {

    @NotBlank(message = "댓글을 입력하세요.")
    @Length(max = 200)
    private String content;

    public CommentRequest(final String content) {
        this.content = content;
    }

    public CommentRequest() {

    }

    public Comment toComment(final Post post, final Member member) {
        return Comment.builder()
                .content(content)
                .member(member)
                .post(post).build();
    }
}
