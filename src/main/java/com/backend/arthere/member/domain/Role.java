package com.backend.arthere.member.domain;

import com.backend.arthere.member.exception.InvalidRoleException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");
    private final String roleName;

    public static Role valueOfName(String roleName) {
        return Arrays.stream(values())
                .filter(value -> value.roleName.equals(roleName))
                .findAny()
                .orElseThrow(InvalidRoleException::new);
    }
}
