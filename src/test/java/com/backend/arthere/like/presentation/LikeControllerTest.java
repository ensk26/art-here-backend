package com.backend.arthere.like.presentation;

import com.backend.arthere.global.ControllerTest;
import com.backend.arthere.global.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.backend.arthere.fixture.MemberFixtures.회원_아이디;
import static com.backend.arthere.fixture.PostFixtures.게시물_아이디;
import static com.backend.arthere.fixture.TokenFixtures.액세스_토큰;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({TestConfig.class})
class LikeControllerTest extends ControllerTest {

    @Test
    @DisplayName("회원은 게시물의 좋아요를 1 증가시킨다.")
    @WithMockUser
    public void 좋아요_1_증가() throws Exception {
        //given
        doNothing().when(likeService).addLike(게시물_아이디, 회원_아이디);
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/like/{postId}", 게시물_아이디)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", 액세스_토큰)
                        .with(csrf().asHeader())
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("like",
                                requestHeaders(
                                        headerWithName("Authorization")
                                                .description("액세스 토큰")
                                ),
                                pathParameters(
                                        parameterWithName("postId")
                                                .description("게시물 아이디")
                                )
                        ));
    }

    @Test
    @DisplayName("비회원이 좋아요 증가 요청을 했을 때 403 에러가 발생한다.")
    public void 비회원이_좋아요_증가_요청_했을_때_에러_발생() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/like/{postId}", 게시물_아이디)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf().asHeader())
        );

        //then
        resultActions.andExpect(status().isForbidden())
                .andDo(print())
                .andDo(
                        document("like/forbidden")
                );
    }

    @Test
    @DisplayName("회원은 게시물의 좋아요를 1 감소시킨다.")
    @WithMockUser
    public void 좋아요_1_감소() throws Exception {
        //given
        doNothing().when(likeService).subtractLike(게시물_아이디, 회원_아이디);
        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/like/{postId}", 게시물_아이디)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", 액세스_토큰)
                        .with(csrf().asHeader())
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("like/subtract",
                                requestHeaders(
                                        headerWithName("Authorization")
                                                .description("액세스 토큰")
                                ),
                                pathParameters(
                                        parameterWithName("postId")
                                                .description("게시물 아이디")
                                )
                        ));
    }



}