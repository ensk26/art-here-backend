package com.backend.arthere.fixture;

import com.backend.arthere.auth.domain.Token;
import com.backend.arthere.auth.dto.request.TokenRequest;
import com.backend.arthere.auth.dto.response.TokenIssueResponse;
import com.backend.arthere.member.domain.Member;

public class TokenFixtures {
    public static final String 리프레시_토큰 = "testRefresh";
    public static final String 액세스_토큰 = "testAccess";
    public static final String 임시_토큰 = "testToken";
    public static final Long 토큰_아이디 = 1L;

    public static Token 토큰(final Member member) {
        return new Token(리프레시_토큰, member);
    }

    public static TokenRequest 토큰_요청() {
        return new TokenRequest(토큰_아이디, 리프레시_토큰);
    }

    public static TokenRequest 리프레시_토큰_없이_요청() {
        return new TokenRequest(토큰_아이디, null);
    }

    public static TokenIssueResponse 토큰_발급_응답() {
        return new TokenIssueResponse(액세스_토큰, 리프레시_토큰);
    }

}
