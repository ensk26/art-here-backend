package com.backend.arthere.member.exception;

public class NotWriterException extends RuntimeException{
    public NotWriterException(final String message) {
        super(message);
    }
    public NotWriterException() {
        this("작성자가 아닙니다.");
    }
}
