package com.backend.arthere.arts.exception;

public class ArtsNotFoundException extends RuntimeException {

    public ArtsNotFoundException(final String message) {
        super(message);
    }

    public ArtsNotFoundException() {
        this("해당 작품 데이터가 존재하지 않습니다.");
    }
}
