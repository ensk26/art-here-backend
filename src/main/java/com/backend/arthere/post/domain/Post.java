package com.backend.arthere.post.domain;

import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.global.domain.BaseEntity;
import com.backend.arthere.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
@Table(name = "post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", length = 100)
    private String title;

    @NotNull
    @Column(name = "content", length = 300)
    private String content;

    @Column(name = "image_url")
    private String imageURL;

    @NotNull
    @Column(name = "like_count")
    private Long likeCount = 0L;

    @NotNull
    @Column(name = "dislike_count")
    private Long dislikeCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arts_id")
    private Arts arts;

    @Builder
    public Post(final Long id, final String title, final String content, final String imageURL, final Long likeCount,
                final Long dislikeCount, final Member member, final Arts arts) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageURL = imageURL;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.member = member;
        this.arts = arts;
    }

}
