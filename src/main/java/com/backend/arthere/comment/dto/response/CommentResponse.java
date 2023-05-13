package com.backend.arthere.comment.dto.response;

import com.backend.arthere.comment.domain.Comment;
import com.backend.arthere.member.dto.response.WriterResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {

    private Long id;

    private String content;

    private LocalDateTime createDate;

    private LocalDateTime revisionDate;

    private boolean sameWriter;

    private WriterResponse writer;

    public CommentResponse(final Long id, final String content, final LocalDateTime createDate,
                           final LocalDateTime revisionDate, final boolean sameWriter, final WriterResponse writer) {
        this.id = id;
        this.content = content;
        this.createDate = createDate;
        this.revisionDate = revisionDate;
        this.sameWriter = sameWriter;
        this.writer = writer;
    }

    public static CommentResponse of(final Comment comment, final Long memberId) {
        final WriterResponse writer = WriterResponse.from(comment.getMember());
        final boolean sameWriter = comment.getMember().isSameId(memberId);
        return new CommentResponse(comment.getId(), comment.getContent(), comment.getCreateDate(),
                comment.getRevisionDate(), sameWriter, writer);
    }
}
