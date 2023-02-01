package com.backend.arthere.auth.presentation;

import com.backend.arthere.auth.application.AuthService;
import com.backend.arthere.auth.dto.request.TokenRequest;
import com.backend.arthere.auth.dto.response.TokenResponse;
import com.backend.arthere.auth.exception.InvalidRefreshTokenException;
import com.backend.arthere.auth.exception.RefreshTokenNotFoundException;
import com.backend.arthere.global.error.ControllerAdvice;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.backend.arthere.fixture.TokenFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(new ControllerAdvice())
                .build();
    }

    @Test
    @DisplayName("리프레시 토큰으로 액세스 토큰 재발급 요청 성공한다.")
    public void 리프레시_토큰으로_재발급_요청_성공 () throws Exception {
        //given
        TokenRequest tokenRequest = 토큰_요청();
        TokenResponse tokenResponse = 토큰_응답();

        given(authService.reissue(any()))
                .willReturn(tokenResponse);

        //when
        ResultActions resultActions = mockMvc.perform(
                        post("/auth/token/reissue")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenRequest))

        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("저장되어 있지 않은 리프레시 토큰으로 재발급을 요청할 경우 예외 발생")
    public void 유효하지_않은_리프레시_토큰으로_재발급_요청시_예외_발생 () throws Exception {
        //given
        TokenRequest tokenRequest = 토큰_요청();

        given(authService.reissue(any()))
                .willThrow(new InvalidRefreshTokenException());

        //when
        ResultActions resultActions = mockMvc.perform(
                        post("/auth/token/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenRequest))
        );

        //then
        resultActions.andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("리프레시 토큰을 입력하지 않고 재발급을 요청할 경우 예외 발생")
    public void 리프레시_토큰을_입력하지_않고_재발급_요청시_예외_발생 () throws Exception {
        //given
        TokenRequest tokenRequest = 리프레시_토큰_없이_요청();

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/auth/token/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenRequest))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
        verify(authService, times(0)).reissue(tokenRequest); //0번 실행
    }

    @Test
    @DisplayName("기간이 만료된 리프레시 토큰으로 재발급을 요청할 경우 예외 발생")
    public void 기간이_만료된_리프레시_토큰으로_재발급_요청시_예외_발생 () throws Exception {
        //given
        TokenRequest tokenRequest = 토큰_요청();

        given(authService.reissue(any()))
                .willThrow(new InvalidRefreshTokenException());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/auth/token/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenRequest))
        );
        //then
        resultActions.andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("리프레시 토큰으로 로그아웃 요청 성공한다.")
    public void 리프레시_토큰으로_로그아웃_요청_성공 () throws Exception {
        //given
        TokenRequest tokenRequest = 토큰_요청();

        doNothing().when(authService).logout(any());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(tokenRequest))
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
        verify(authService).logout(tokenRequest.getRefreshToken()); //한번 실행
    }

    @Test
    @DisplayName("리프레시 토큰을 입력하지 않고 로그아웃을 요청할 경우 예외 발생")
    public void 리프레시_토큰을_입력하지_않고_로그아웃_요청시_예외_발생() throws Exception {
        //given
        TokenRequest tokenRequest = 리프레시_토큰_없이_요청();
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(tokenRequest))
        );
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
        verify(authService, times(0)).logout(tokenRequest.getRefreshToken()); //0번 실행
    }

    @Test
    @DisplayName("저장되어 있지 않은 리프레시 토큰으로 로그아웃을 요청할 경우 예외 발생")
    public void 존재하지_않은_리프레시_토큰으로_로그아웃_요청시_예외_발생() throws Exception {
        //given
        TokenRequest tokenRequest = 토큰_요청();

        doThrow(new RefreshTokenNotFoundException()).when(authService).logout(any());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(tokenRequest))
        );
        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());
        verify(authService).logout(tokenRequest.getRefreshToken()); //한번 실행
    }
}