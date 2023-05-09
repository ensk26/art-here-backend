package com.backend.arthere.comment.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(final String message) {
        super(message);
    }

    public CommentNotFoundException() {
        this("존재하지 않는 댓글입니다.");
    }
}
