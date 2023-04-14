package com.backend.arthere.details.exception;


public class InvalidSizeException extends RuntimeException {

    public InvalidSizeException(final String message) {
        super(message);
    }

    public InvalidSizeException() {
        this("1 이상 10 이하의 size를 입력해주세요.");
    }
}