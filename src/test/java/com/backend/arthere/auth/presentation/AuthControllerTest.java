package com.backend.arthere.auth.presentation;


import com.backend.arthere.auth.dto.request.TokenIssueRequest;
import com.backend.arthere.auth.dto.request.TokenRequest;
import com.backend.arthere.auth.dto.response.TokenIssueResponse;
import com.backend.arthere.auth.dto.response.TokenResponse;
import com.backend.arthere.auth.exception.InvalidRefreshTokenException;
import com.backend.arthere.auth.exception.InvalidTokenException;
import com.backend.arthere.auth.exception.RefreshTokenNotFoundException;
import com.backend.arthere.global.ControllerTest;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;


import static com.backend.arthere.fixture.TokenFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
        TokenResponse tokenResponse = new TokenResponse(액세스_토큰);

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
    @DisplayName("저장되어 있지 않은 리프레시 토큰으로 재발급을 요청할 경우 404 에러가 발생한다.")
    public void 유효하지_않은_리프레시_토큰으로_재발급_요청시_에러_발생 () throws Exception {
        //given
        TokenRequest tokenRequest = 토큰_요청();

        given(authService.reissue(any()))
                .willThrow(new RefreshTokenNotFoundException());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auth/token/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenRequest))
                        .with(csrf().asHeader())
        );

        //then
        resultActions.andExpect(status().isNotFound())
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
    @DisplayName("로그인 후 토큰 발급 요청시 성공한다.")
    public void 토큰_발급_요청_성공() throws Exception {
        //given
        TokenIssueRequest tokenIssueRequest = new TokenIssueRequest(토큰_아이디);
        TokenIssueResponse tokenIssueResponse = 토큰_발급_응답();

        given(authService.issue(any(), any()))
                .willReturn(tokenIssueResponse);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auth/token/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenIssueRequest))
                        .header("Token", 임시_토큰)
                        .with(csrf().asHeader())
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("auth/issue",
                                requestFields(
                                        fieldWithPath("id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("아이디")
                                ),
                                requestHeaders(
                                        headerWithName("Token")
                                                .description("토큰")
                                ),
                                responseFields(
                                        fieldWithPath("accessToken")
                                                .type(JsonFieldType.STRING)
                                                .description("액세스 토큰"),
                                        fieldWithPath("refreshToken")
                                                .type(JsonFieldType.STRING)
                                                .description("리프레시 토큰")
                                )
                        )
                );
    }

    @Test
    @DisplayName("기간이 만료된 토큰 혹은 올바르지 않은 사용자가 발급 요청시 401 에러가 발생한다.")
    public void 기간이_만료된_토큰으로_발급_요청시_에러_발생() throws Exception {
        //given
        TokenIssueRequest tokenIssueRequest = new TokenIssueRequest(토큰_아이디);

        given(authService.issue(anyLong(), any()))
                .willThrow(new InvalidTokenException());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auth/token/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenIssueRequest))
                        .header("Token", 임시_토큰)
                        .with(csrf().asHeader())
        );

        //then
        resultActions.andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(
                        document("auth/issue/expired")
                );
    }

    @Test
    @DisplayName("토큰을 입력하지 않고 토큰 발급 요청시 400 에러가 발생한다.")
    public void 토큰을_입력하지_않고_토큰_발급_요청시_에러_발생() throws Exception {
        //given
        TokenIssueRequest tokenIssueRequest = new TokenIssueRequest(토큰_아이디);
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auth/token/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(tokenIssueRequest))
                        .with(csrf().asHeader())
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("auth/issue/invalid")
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