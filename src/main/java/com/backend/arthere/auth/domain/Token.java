package com.backend.arthere.auth.domain;

import com.backend.arthere.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "refresh_token")
    private String refreshToken;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Token(final String refreshToken, final Member member) {
        this.refreshToken = refreshToken;
        this.member = member;
    }

    public void changeRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isSameRefreshToken(final String refreshToken) {
        if(refreshToken.equals(this.refreshToken)) {
            return true;
        }
        return false;
    }
}