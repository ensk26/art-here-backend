package com.backend.arthere.post.dto.response;

import com.backend.arthere.member.dto.response.WriterResponse;
import com.backend.arthere.post.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {


    private String content;

    private Long likeCount;

    private Long unlikeCount;

    private LocalDateTime createDate;

    private LocalDateTime revisionDate;

    private WriterResponse writer;

    private PostResponse(final String content, final Long likeCount, final Long unlikeCount,
                         final LocalDateTime createDate, final LocalDateTime revisionDate, final WriterResponse writer) {
        this.content = content;
        this.likeCount = likeCount;
        this.unlikeCount = unlikeCount;
        this.createDate = createDate;
        this.revisionDate = revisionDate;
        this.writer = writer;
    }

    public static PostResponse from(final Post post) {

        final WriterResponse writer = WriterResponse.from(post.getMember());

        return new PostResponse(post.getContent(), post.getLikeCount(), post.getUnlikeCount(),
                post.getCreateDate(), post.getRevisionDate(), writer);
    }

    public void setDate(final LocalDateTime createDate, final LocalDateTime revisionDate) {
        this.createDate = createDate;
        this.revisionDate = revisionDate;
    }
}
