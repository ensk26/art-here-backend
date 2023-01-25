package com.backend.arthere.auth.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TokenResponse {
    private String accessToken;

    public TokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}

