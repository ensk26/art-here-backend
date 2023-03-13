package com.backend.arthere.auth.exception;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException(final String message) {
        super(message);
    }

    public InvalidRefreshTokenException() {
        this("유효하지 않은 리프레시 토큰입니다.");
    }
}
