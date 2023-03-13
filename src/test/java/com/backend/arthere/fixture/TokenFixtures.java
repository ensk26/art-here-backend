package com.backend.arthere.fixture;

import com.backend.arthere.auth.domain.Token;
import com.backend.arthere.auth.dto.request.TokenIssueRequest;
import com.backend.arthere.auth.dto.request.TokenRequest;
import com.backend.arthere.auth.dto.response.TokenIssueResponse;
import com.backend.arthere.auth.dto.response.TokenResponse;
import com.backend.arthere.member.domain.Member;

public class TokenFixtures {
    private static final String REFRESH_TOKEN = "testRefresh";
    private static final String ACCESS_TOKEN = "testAccess";
    private static final String TOKEN = "testToken";
    private static final Long ID = 1L;

    public static Token 토큰(final Member member) {
        return new Token(REFRESH_TOKEN, member);
    }

    public static TokenRequest 토큰_요청() {
        return new TokenRequest(ID, REFRESH_TOKEN);
    }

    public static TokenRequest 리프레시_토큰_없이_요청() {
        return new TokenRequest(ID, null);
    }

    public static TokenIssueRequest 토큰_발급_요청() {
        return new TokenIssueRequest(ID, TOKEN);
    }

    public static TokenIssueResponse 토큰_발급_응답() {
        return new TokenIssueResponse(ACCESS_TOKEN, REFRESH_TOKEN);
    }

    public static TokenResponse 토큰_응답() {
        return new TokenResponse(ACCESS_TOKEN);
    }
}
