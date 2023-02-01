package com.backend.arthere.fixture;

import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.Role;
import com.backend.arthere.member.domain.SocialType;

public class MemberFixtures {

    private static final String EMAIL = "123@gmail.com";
    private static final String USER_NAME = "회원";
    private static final String ADMIN_NAME = "관리자";
    private static final String PROFILE = "사진1";
    private static final Role USER_ROLE = Role.USER;
    private static final Role ADMIN_ROLE = Role.USER;
    private static final SocialType SOCIAL_TYPE = SocialType.GOOGLE;

    public static Member 관리자() {
        return new Member(EMAIL, ADMIN_NAME, PROFILE, ADMIN_ROLE, SOCIAL_TYPE);
    }

    public static  Member 회원() {
        return new Member(EMAIL, USER_NAME, PROFILE, USER_ROLE, SOCIAL_TYPE);
    }
}
