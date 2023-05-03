package com.backend.arthere.satisfactions.exception;

public class InvalidSatisfactionException extends RuntimeException {

    public InvalidSatisfactionException(final String message) {
        super(message);
    }

    public InvalidSatisfactionException() {
        this("잘못된 만족도 이름입니다.");
    }
}
