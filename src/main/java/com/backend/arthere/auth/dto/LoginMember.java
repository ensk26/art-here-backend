package com.backend.arthere.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginMember {
    private Long id;

    public LoginMember(final Long id) {
        this.id = id;
    }
}
