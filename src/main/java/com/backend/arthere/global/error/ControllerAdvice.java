package com.backend.arthere.global.error;

import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.arts.exception.InvalidCategoryException;
import com.backend.arthere.auth.exception.FailedTokenAuthenticationException;
import com.backend.arthere.auth.exception.InvalidRefreshTokenException;
import com.backend.arthere.auth.exception.RefreshTokenNotFoundException;
import com.backend.arthere.details.exception.DetailsNotFoundException;
import com.backend.arthere.member.exception.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({InvalidRefreshTokenException.class, FailedTokenAuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleInvalidRefreshToken(final RuntimeException error) {
        ErrorResponse errorResponse = new ErrorResponse(error.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValid(final MethodArgumentNotValidException error) {

        StringBuilder sb = new StringBuilder();
        error.getBindingResult().getAllErrors().forEach(e -> sb.append(e.getDefaultMessage())
                .append(System.lineSeparator()));

        ErrorResponse errorResponse = new ErrorResponse(sb.toString());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InvalidCategoryException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(final RuntimeException error) {
        ErrorResponse errorResponse = new ErrorResponse(error.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({RefreshTokenNotFoundException.class, MemberNotFoundException.class,
            ArtsNotFoundException.class, DetailsNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(final RuntimeException error) {
        ErrorResponse errorResponse = new ErrorResponse(error.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
