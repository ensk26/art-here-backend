package com.backend.arthere.starRatings.domain;

import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StarRatings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arts_id")
    private Arts arts;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @Min(0)
    @Max(5)
    private Integer starRating = 0;

    @Builder
    public StarRatings(Arts arts, Member member, Integer starRating) {
        this.arts = arts;
        this.member = member;
        this.starRating = starRating;
    }
}
