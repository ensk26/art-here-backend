package com.backend.arthere.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenIssueRequest {

    @NotNull(message = "아이디를 입력하세요.")
    private Long id;

}
