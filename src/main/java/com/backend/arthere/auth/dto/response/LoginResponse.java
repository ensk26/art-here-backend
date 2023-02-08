package com.backend.arthere.auth.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long id;

    public LoginResponse(final String accessToken, final String refreshToken, final Long id) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
    }
}
