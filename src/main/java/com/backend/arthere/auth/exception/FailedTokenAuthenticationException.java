package com.backend.arthere.auth.exception;

public class FailedTokenAuthenticationException extends RuntimeException{
    public FailedTokenAuthenticationException(final String message) {
        super(message);
    }

    public FailedTokenAuthenticationException() {
        this("토큰 인증에 실패하였습니다.");
    }
}