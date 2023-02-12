package com.backend.arthere.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberNameRequest {
    @NotBlank(message = "회원 이름을 입력하세요.")
    private String name;
}
