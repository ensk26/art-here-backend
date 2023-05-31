package com.backend.arthere.member.exception;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(final String message) {
        super(message);
    }

    public InvalidRoleException() {
        this("잘못된 Role 이름입니다.");
    }
}
