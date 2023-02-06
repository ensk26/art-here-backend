package com.backend.arthere.auth.presentation;

import com.backend.arthere.auth.application.AuthService;
import com.backend.arthere.auth.dto.request.TokenRequest;
import com.backend.arthere.auth.dto.response.TokenResponse;
import com.backend.arthere.auth.exception.InvalidRefreshTokenException;
import com.backend.arthere.auth.exception.RefreshTokenNotFoundException;
import com.backend.arthere.global.ControllerTest;
import com.backend.arthere.global.RestDocsConfig;
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
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.swing.*;

import static com.backend.arthere.fixture.TokenFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WithMockUser
class AuthControllerTest extends ControllerTest {

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
                post("/api/auth/token/reissue")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenRequest))
                        .with(csrf().asHeader())

        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("auth/reissue",
                                requestFields(
                                        fieldWithPath("id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("아이디"),
                                        fieldWithPath("refreshToken")
                                                .type(JsonFieldType.STRING)
                                                .description("리프레시 토큰")
                                ),
                                responseFields(
                                      fieldWithPath("accessToken")
                                              .type(JsonFieldType.STRING)
                                              .description("액세스 토큰")
                                )
                ));
    }

    @Test
    @DisplayName("저장되어 있지 않은 리프레시 토큰으로 재발급을 요청할 경우 401 에러가 발생한다.")
    public void 유효하지_않은_리프레시_토큰으로_재발급_요청시_에러_발생 () throws Exception {
        //given
        TokenRequest tokenRequest = 토큰_요청();

        given(authService.reissue(any()))
                .willThrow(new InvalidRefreshTokenException());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auth/token/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenRequest))
                        .with(csrf().asHeader())
        );

        //then
        resultActions.andExpect(status().isUnauthorized())
                .andDo(print());

    }

    @Test
    @DisplayName("리프레시 토큰을 입력하지 않고 재발급을 요청할 경우 400 에러가 발생한다.")
    public void 리프레시_토큰을_입력하지_않고_재발급_요청시_에러_발생 () throws Exception {
        //given
        TokenRequest tokenRequest = 리프레시_토큰_없이_요청();

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auth/token/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenRequest))
                        .with(csrf().asHeader())
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("auth/reissue/invalid")
                );
        verify(authService, times(0)).reissue(tokenRequest); //0번 실행
    }

    @Test
    @DisplayName("기간이 만료된 리프레시 토큰으로 재발급을 요청할 경우 401 에러가 발생한다.")
    public void 기간이_만료된_리프레시_토큰으로_재발급_요청시_에러_발생 () throws Exception {
        //given
        TokenRequest tokenRequest = 토큰_요청();

        given(authService.reissue(any()))
                .willThrow(new InvalidRefreshTokenException());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auth/token/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenRequest))
                        .with(csrf().asHeader())
        );
        
        //then
        resultActions.andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(
                        document("auth/reissue/expired")
                );
    }

    @Test
    @DisplayName("리프레시 토큰으로 로그아웃 요청 성공한다.")
    public void 리프레시_토큰으로_로그아웃_요청_성공 () throws Exception {
        //given
        TokenRequest tokenRequest = 토큰_요청();

        doNothing().when(authService).logout(any());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenRequest))
                        .with(csrf().asHeader())
        );
        
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("auth/logout",
                                requestFields(
                                        fieldWithPath("id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("아이디"),
                                        fieldWithPath("refreshToken")
                                                .type(JsonFieldType.STRING)
                                                .description("리프레시 토큰")
                                )
                ));

        verify(authService).logout(tokenRequest.getRefreshToken()); //한번 실행
    }

    @Test
    @DisplayName("리프레시 토큰을 입력하지 않고 로그아웃을 요청할 경우 400 에러가 발생한다.")
    public void 리프레시_토큰을_입력하지_않고_로그아웃_요청시_에러_발생() throws Exception {
        //given
        TokenRequest tokenRequest = 리프레시_토큰_없이_요청();
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenRequest))
                        .with(csrf().asHeader())
        );
        
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("auth/logout/invalid")
                );

        verify(authService, times(0)).logout(tokenRequest.getRefreshToken()); //0번 실행
    }

    @Test
    @DisplayName("저장되어 있지 않은 리프레시 토큰으로 로그아웃을 요청할 경우 404 에러가 발생한다.")
    public void 존재하지_않은_리프레시_토큰으로_로그아웃_요청시_에러_발생() throws Exception {
        //given
        TokenRequest tokenRequest = 토큰_요청();

        doThrow(new RefreshTokenNotFoundException()).when(authService).logout(any());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenRequest))
                        .with(csrf().asHeader())
        );
        
        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("auth/logout/notFound")
                );

        verify(authService).logout(tokenRequest.getRefreshToken()); //한번 실행
    }
}