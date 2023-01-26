package com.backend.arthere.auth.application;

import com.backend.arthere.auth.domain.UserPrincipal;
import com.backend.arthere.auth.dto.OAuth2Attributes;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.member.domain.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() - OAuth2 로그인 요청 한다.");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(socialType, userNameAttributeName, attributes);
        Member member = findMember(socialType, oAuth2Attributes);

        return UserPrincipal.create(member, attributes);
    }

    public SocialType getSocialType(String registrationId) {
        if (SocialType.GOOGLE.isSameSocialName(registrationId)) {
            return SocialType.GOOGLE;
        }
        return null;
    }

    private Member findMember(SocialType socialType, OAuth2Attributes oAuth2Attribute) {
        Member findMember = memberRepository.findBySocialTypeAndEmail(socialType,
                        oAuth2Attribute.getOAuth2UserInfo().getEmail())
                .orElse(null);

        if (findMember == null) {
            return saveMember(socialType, oAuth2Attribute);
        }
        return findMember;
    }

    private Member saveMember(SocialType socialType, OAuth2Attributes oAuth2Attributes) {
        Member saveMember = oAuth2Attributes.toEntity(socialType);
        log.info("saveMember() 생성된 Member 아이디 확인 {}", oAuth2Attributes.getOAuth2UserInfo().getId());
        return memberRepository.save(saveMember);
    }
}
