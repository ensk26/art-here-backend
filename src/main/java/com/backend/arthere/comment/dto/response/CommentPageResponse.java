package com.backend.arthere.comment.dto.response;

import com.backend.arthere.comment.domain.Comment;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentPageResponse {
    private boolean hasNext;
    private List<CommentResponse> commentResponses;

    public CommentPageResponse(final Boolean hasNext, final List<CommentResponse> commentResponses) {
        this.hasNext = hasNext;
        this.commentResponses = commentResponses;
    }

    public static CommentPageResponse of(final Slice<Comment> comments, final Long memberId) {
        List<CommentResponse> commentResponses = comments.getContent()
                .stream()
                .map(comment -> CommentResponse.of(comment, memberId))
                .collect(Collectors.toList());

        return new CommentPageResponse(comments.hasNext(), commentResponses);
    }
}
