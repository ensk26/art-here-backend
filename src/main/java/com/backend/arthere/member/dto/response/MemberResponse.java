package com.backend.arthere.member.dto.response;

import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.SocialType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponse {
    private Long id;
    private String email;
    private String name;
    private String profile;
    private SocialType socialType;

    private MemberResponse(Long id, String email, String name,
                          String profile, SocialType socialType) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.profile = profile;
        this.socialType = socialType;
    }

    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getName(),
                member.getProfile(), member.getSocialType());
    }
}
