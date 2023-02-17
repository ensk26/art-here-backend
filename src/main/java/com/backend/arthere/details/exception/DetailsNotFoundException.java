package com.backend.arthere.details.exception;

public class DetailsNotFoundException extends RuntimeException {

    public DetailsNotFoundException(final String message) {
        super(message);
    }

    public DetailsNotFoundException() {
        this("존재하지 않는 작품입니다.");
    }
}
