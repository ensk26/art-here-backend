package com.backend.arthere.fixture;

import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.Role;
import com.backend.arthere.member.domain.SocialType;

public class MemberFixtures {

    public static final Long 회원_아이디 = 1L;
    public static final Long 관리자_아이디 = 2L;

    public static final String 회원_이메일 = "123@gmail.com";
    public static final String 관리자_이메일 = "456@gmail.com";

    public static final String 회원_이름 = "회원";
    public static final String 회원_이름_수정 = "회원수정";
    public static final String 관리자_이름 = "관리자";

    public static final String 회원_프로필 = "사진1";
    public static final String 관리자_프로필 = "사진2";

    public static final SocialType 소셜타입 = SocialType.GOOGLE;

    public static Member 관리자() {
        return new Member(관리자_아이디, 관리자_이메일, 관리자_이름, 관리자_프로필,
                Role.ADMIN, 소셜타입);
    }

    public static  Member 회원() {
        return Member.builder()
                .email(회원_이메일).name(회원_이름)
                .profile(회원_프로필).role(Role.USER)
                .socialType(소셜타입).build();
    }

    public static Member 회원(final Long id) {
        return new Member(id, 회원_이메일, 회원_이름, 회원_프로필, Role.USER, 소셜타입);

    }

}
