package com.backend.arthere.satisfactions.domain;

import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Satisfactions {

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
    @Column(name = "satisfaction", length = 50)
    @Enumerated(EnumType.STRING)
    private SatisfactionType satisfactionType;

    @Builder
    public Satisfactions(Arts arts, Member member, SatisfactionType satisfactionType) {
        this.arts = arts;
        this.member = member;
        this.satisfactionType = satisfactionType;
    }
}
