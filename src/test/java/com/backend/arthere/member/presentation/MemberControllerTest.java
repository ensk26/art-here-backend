package com.backend.arthere.member.presentation;

import com.backend.arthere.auth.dto.LoginMember;
import com.backend.arthere.global.ControllerTest;
import com.backend.arthere.member.dto.request.MemberNameRequest;
import com.backend.arthere.member.dto.response.MemberResponse;
import com.backend.arthere.member.exception.MemberNotFoundException;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import static com.backend.arthere.fixture.MemberFixtures.회원_응답;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
class MemberControllerTest extends ControllerTest {

    @Test
    @DisplayName("회원 조회를 성공한다.")
    public void 회원_조회_성공() throws Exception {
        //given
        MemberResponse memberResponse = 회원_응답();
        String accessToken = "Bearer testAccess";

        given(memberService.findMember(any()))
                .willReturn(memberResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/member")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .with(csrf().asHeader())
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/member",
                                requestHeaders(
                                        headerWithName("Authorization")
                                                .description("액세스 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("아이디"),
                                        fieldWithPath("email")
                                                .type(JsonFieldType.STRING)
                                                .description("이메일"),
                                        fieldWithPath("name")
                                                .type(JsonFieldType.STRING)
                                                .description("이름"),
                                        fieldWithPath("profile")
                                                .type(JsonFieldType.STRING)
                                                .description("이미지 경로"),
                                        fieldWithPath("socialType")
                                                .type(JsonFieldType.STRING)
                                                .description("소셜 타입")
                                )
                ));
    }

    @Test
    @DisplayName("저장되어 있지 않은 회원 조회시 404 에러가 발생한다.")
    public void 저장되어_있지_않은_회원_조회시_에러_발생() throws Exception {
        //given
        String accessToken = "Bearer testAccess";
        LoginMember loginMember = new LoginMember(1L);
        given(authenticationArgumentResolver.supportsParameter(any()))
                .willReturn(true);
        given(authenticationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(loginMember);

        given(memberService.findMember(any()))
                .willThrow(new MemberNotFoundException());

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/member")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .with(csrf().asHeader())
        );

        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("api/member/notFound")
                );
    }

    @Test
    @DisplayName("회원 이름을 변경한다.")
    public void 회원_이름_변경() throws Exception {
        //given
        MemberNameRequest memberNameRequest = new MemberNameRequest("이름변경");
        String accessToken = "Bearer testAccess";

        doNothing().when(memberService).updateName(1L, memberNameRequest.getName());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/member/name")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(new Gson().toJson(memberNameRequest))
                        .with(csrf().asHeader())
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/memberName",
                                requestHeaders(
                                        headerWithName("Authorization")
                                                .description("액세스 토큰")
                                ),
                                requestFields(
                                        fieldWithPath("name")
                                                .type(JsonFieldType.STRING)
                                                .description("이름")
                                )
                        ));
    }

    @Test
    @DisplayName("회원 이름을 입력하지 않고 변경하려고 하면 예외가 발생한다.")
    public void 이름_입력하지_않고_변경하려고_하면_예외_발생() throws Exception {
        //given
        MemberNameRequest memberNameRequest = new MemberNameRequest(null);
        String accessToken = "Bearer testAccess";

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/member/name")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(new Gson().toJson(memberNameRequest))
                        .with(csrf().asHeader())
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("api/memberName/invalid")
                );
    }

}