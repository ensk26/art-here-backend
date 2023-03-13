package com.backend.arthere.auth.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TokenIssueResponse {

    private String accessToken;

    private String refreshToken;

    public TokenIssueResponse(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
