package com.backend.arthere.comment.domain;

import com.backend.arthere.global.domain.BaseEntity;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.post.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "content", length = 200)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Comment(final Long id, final String content, final Member member, final Post post) {
        this.content = content;
        this.member = member;
        this.post = post;
    }

    public boolean isWriter(final Member member) {
        return this.member.equals(member);
    }

    public void updateContent(final String content) {
        this.content = content;
    }
}
