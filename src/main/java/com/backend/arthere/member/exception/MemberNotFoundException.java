package com.backend.arthere.member.exception;

public class MemberNotFoundException extends RuntimeException{
    public MemberNotFoundException(final String message) {
        super(message);
    }
    public MemberNotFoundException() {
        this("존재하지 않는 회원입니다.");
    }
}
