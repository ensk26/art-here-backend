package com.backend.arthere.post.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(final String message) {
        super(message);
    }

    public PostNotFoundException() {
        this("존재하지 않는 게시물입니다.");
    }
}

