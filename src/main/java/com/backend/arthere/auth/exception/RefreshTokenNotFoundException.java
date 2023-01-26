package com.backend.arthere.auth.exception;

public class RefreshTokenNotFoundException extends RuntimeException{
    public RefreshTokenNotFoundException(final String message) {
        super(message);
    }

    public RefreshTokenNotFoundException() {
        this("존재하지 않는 리프레시 토큰입니다.");
    }
}