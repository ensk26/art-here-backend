package com.backend.arthere.details.exception;


public class InvalidFormatException extends RuntimeException {

    public InvalidFormatException(final String message) {
        super(message);
    }

    public InvalidFormatException() {
        this("유효한 숫자를 입력하세요.");
    }
}