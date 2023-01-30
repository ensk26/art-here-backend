package com.backend.arthere.auth.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CookieUtilsTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private static final String REFRESH_TOKEN = "testRefresh";
    private static final String REFRESH_TOKEN_NAME = "refreshToken";

    @BeforeEach
    public void init() {
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
    }

    @Test
    @DisplayName("쿠키에 저장된 리프레시 토큰을 가져오는데 성공한다.")
    public void 쿠키에_저장된_리프레시_토큰_가져오기() throws Exception {
        //given
        Cookie testCookie = new Cookie(REFRESH_TOKEN_NAME, REFRESH_TOKEN);
        request.setCookies(testCookie);

        //when
        Optional<Cookie> cookie = CookieUtils.getCookie(request, REFRESH_TOKEN_NAME);

        //then
        assertThat(cookie).isNotEmpty();
        assertThat(cookie.get().getName()).isEqualTo(REFRESH_TOKEN_NAME);
        assertThat(cookie.get().getValue()).isEqualTo(REFRESH_TOKEN);
    }

    @Test
    @DisplayName("응답 서블릿에 요청 서블릿의 토큰 값을 ''로 저장한다.")
    public void 리프레시_토큰_쿠키_값_비우기() throws Exception {
        //given
        Cookie testCookie = new Cookie(REFRESH_TOKEN_NAME, REFRESH_TOKEN);
        request.setCookies(testCookie);

        //when
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN_NAME);

        //then
        assertThat(response.getCookie(REFRESH_TOKEN_NAME).getValue()).isEqualTo("");
    }

    @Test
    @DisplayName("응답 서블릿에 리프리시 토큰 쿠키를 저장한다.")
    public void 응답_서블릿에_리프레시_토큰_쿠키_저장() throws Exception {
        //given //when
        CookieUtils.addCookie(response, REFRESH_TOKEN_NAME, REFRESH_TOKEN, 10);

        //then
        assertThat(response.getCookie(REFRESH_TOKEN_NAME).getValue()).isEqualTo(REFRESH_TOKEN);
        assertThat(response.getCookie(REFRESH_TOKEN_NAME).getMaxAge()).isEqualTo(10);
    }

}