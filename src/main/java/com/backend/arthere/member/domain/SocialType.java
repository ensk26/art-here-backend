package com.backend.arthere.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    GOOGLE("google");
    private final String socialName;
    public boolean isSameSocialName(String socialName) {
        return this.socialName.equals(socialName);
    }
}
