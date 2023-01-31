package com.backend.arthere.global.error;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String errorMessage;
    ErrorResponse(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
