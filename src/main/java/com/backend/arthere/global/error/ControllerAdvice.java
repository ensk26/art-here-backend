package com.backend.arthere.global.error;

import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.arts.exception.InvalidCategoryException;
import com.backend.arthere.auth.exception.InvalidRefreshTokenException;
import com.backend.arthere.auth.exception.InvalidTokenException;
import com.backend.arthere.auth.exception.RefreshTokenNotFoundException;
import com.backend.arthere.details.exception.DetailsNotFoundException;
import com.backend.arthere.member.exception.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice
public class ControllerAdvice {

    private final String MISSING_REQUEST_HEADER = "요청 헤더가 존재하지 않습니다.";
    private final String MISSING_REQUEST_PARAMETER = "요청 파라미터가 존재하지 않습니다.";
    private final String TYPE_MISS_MATCH = "요청 형식이 올바르지 않습니다.";
    private final String INTERNAL_SERVER_ERROR_MESSAGE = "서버 에러가 발생했습니다.";

    @ExceptionHandler({InvalidRefreshTokenException.class, InvalidTokenException.class})
    public ResponseEntity<ErrorResponse> handleInvalidToken(final RuntimeException error) {
        ErrorResponse errorResponse = new ErrorResponse(error.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<ErrorResponse> handleBind(final BindException error) {
        StringBuilder sb = new StringBuilder();
        error.getBindingResult().getAllErrors().forEach(e -> sb.append(e.getDefaultMessage())
                .append(System.lineSeparator()));

        ErrorResponse errorResponse = new ErrorResponse(sb.toString());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({MissingRequestHeaderException.class})
    public ResponseEntity<ErrorResponse> handleHeader(final Exception error) {
        ErrorResponse errorResponse = new ErrorResponse(MISSING_REQUEST_HEADER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResponse> handleParameter(final Exception error) {
        ErrorResponse errorResponse = new ErrorResponse(MISSING_REQUEST_PARAMETER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleTypeMismatch(final Exception error) {
        ErrorResponse errorResponse = new ErrorResponse(TYPE_MISS_MATCH);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({InvalidCategoryException.class})
    public ResponseEntity<ErrorResponse> handleInvalid(final RuntimeException error) {
        ErrorResponse errorResponse = new ErrorResponse(error.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({RefreshTokenNotFoundException.class, MemberNotFoundException.class,
            ArtsNotFoundException.class, DetailsNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(final RuntimeException error) {
        ErrorResponse errorResponse = new ErrorResponse(error.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception error) {
        ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR_MESSAGE);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
