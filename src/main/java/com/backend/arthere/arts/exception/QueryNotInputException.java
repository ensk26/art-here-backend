package com.backend.arthere.arts.exception;

public class QueryNotInputException extends RuntimeException {

    public QueryNotInputException(final String message) {
        super(message);
    }

    public QueryNotInputException() {
        this("검색어를 입력하세요.");
    }
}
