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

    private boolean sameWriter;
    private WriterResponse writer;

    private PostResponse(final String content, final Long likeCount, final Long dislikeCount,
                         final LocalDateTime createDate, final LocalDateTime revisionDate,
                         final boolean likeState, final boolean dislikeState, final boolean sameWriter,
                         final WriterResponse writer) {
        this.content = content;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.createDate = createDate;
        this.revisionDate = revisionDate;
        this.likeState = likeState;
        this.dislikeState = dislikeState;
        this.sameWriter = sameWriter;
        this.writer = writer;
    }

    public static PostResponse of(final Post post, final boolean likeState, final boolean dislikeState,
                                  final Long memberId) {

        final WriterResponse writer = WriterResponse.from(post.getMember());
        final boolean sameWriter = post.getMember().isSameId(memberId);
        return new PostResponse(post.getContent(), post.getLikeCount(), post.getDislikeCount(),
                post.getCreateDate(), post.getRevisionDate(), likeState, dislikeState, sameWriter, writer);
    }

    public void setDate(final LocalDateTime createDate, final LocalDateTime revisionDate) {
        this.createDate = createDate;
        this.revisionDate = revisionDate;
    }
}
