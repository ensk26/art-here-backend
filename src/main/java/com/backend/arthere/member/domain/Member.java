package com.backend.arthere.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    private String profile;

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "create_date")
    private LocalDateTime createDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @Builder
    public Member(final String email, final String name, final String profile,
                  final Role role, final SocialType socialType) {
        this.email = email;
        this.name = name;
        this.profile = profile;
        this.role = role;
        this.socialType = socialType;
    }

    public String getRoleName() {
        return this.role.getRoleName();
    }

}
