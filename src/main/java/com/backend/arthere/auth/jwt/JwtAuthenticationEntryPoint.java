package com.backend.arthere.auth.jwt;

import com.backend.arthere.global.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  인증 과정에 실패하거나 인증을 위한 헤더정보를 보내지 않은 경우
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = new ErrorResponse("토큰 인증에 실패하였습니다.");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter()
                .write(objectMapper.writeValueAsString(errorResponse));
    }
}