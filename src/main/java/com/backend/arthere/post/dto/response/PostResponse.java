package com.backend.arthere.post.dto.response;

import com.backend.arthere.member.dto.response.WriterResponse;
import com.backend.arthere.post.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private String content;

    private Long likeCount;

    private Long dislikeCount;

    private LocalDateTime createDate;

    private LocalDateTime revisionDate;

    private boolean likeState;

    private boolean dislikeState;

    private WriterResponse writer;

    private PostResponse(final String content, final Long likeCount, final Long dislikeCount,
                         final LocalDateTime createDate, final LocalDateTime revisionDate,
                         final boolean likeState, final boolean dislikeState, final WriterResponse writer) {
        this.content = content;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.createDate = createDate;
        this.revisionDate = revisionDate;
        this.likeState = likeState;
        this.dislikeState = dislikeState;
        this.writer = writer;
    }

    public static PostResponse of(final Post post, final boolean likeState, final boolean dislikeState) {

        final WriterResponse writer = WriterResponse.from(post.getMember());

        return new PostResponse(post.getContent(), post.getLikeCount(), post.getDislikeCount(),
                post.getCreateDate(), post.getRevisionDate(), likeState, dislikeState, writer);
    }

    public void setDate(final LocalDateTime createDate, final LocalDateTime revisionDate) {
        this.createDate = createDate;
        this.revisionDate = revisionDate;
    }
}
