package com.backend.arthere.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequest {

    @NotBlank(message = "리프레시 토큰을 입력하세요.")
    private String refreshToken;

}