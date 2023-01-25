package com.backend.arthere.auth.dto;

import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.Role;
import com.backend.arthere.member.domain.SocialType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2Attributes {
    private String  attributeKey;
    private OAuth2UserInfo oAuth2UserInfo;

    @Builder
    public OAuth2Attributes(String attributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.attributeKey = attributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static OAuth2Attributes of(SocialType socialType, String userNameAttributeName,
                                      Map<String, Object> attributes) {
        if(socialType == SocialType.GOOGLE) {
            return ofGoogle(userNameAttributeName,attributes);
        }
        return null;
    }

    private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .attributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public Member toEntity(SocialType socialType) {
        return Member.builder()
                .email(oAuth2UserInfo.getEmail())
                .name(oAuth2UserInfo.getName())
                .profile(oAuth2UserInfo.getProfile())
                .role(Role.USER)
                .socialType(socialType)
                .build();
    }
}
