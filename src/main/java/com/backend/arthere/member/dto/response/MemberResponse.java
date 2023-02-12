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

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.profile = member.getProfile();
        this.socialType = member.getSocialType();
    }

    public MemberResponse(Long id, String email, String name,
                          String profile, SocialType socialType) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.profile = profile;
        this.socialType = socialType;
    }
}
