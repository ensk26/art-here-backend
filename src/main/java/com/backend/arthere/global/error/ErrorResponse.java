package com.backend.arthere.global.error;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    ErrorResponse(final String message) {
        this.message = message;
    }
}
