package com.backend.arthere.dislike.presentation;

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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({TestConfig.class})
class DislikeControllerTest extends ControllerTest {
    
    @Test
    @DisplayName("회원은 게시물의 싫어요를 1 증가시킨다.")
    @WithMockUser
    public void 싫어요_1_증가() throws Exception {
        //given
        doNothing().when(dislikeService).addDislike(게시물_아이디, 회원_아이디);
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/dislike/{postId}", 게시물_아이디)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", 액세스_토큰)
                        .with(csrf().asHeader())
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("dislike",
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
    @DisplayName("회원은 게시물의 싫어요를 1 감소시킨다.")
    @WithMockUser
    public void 싫어요_1_감소() throws Exception {
        //given
        doNothing().when(dislikeService).subtractDislike(게시물_아이디, 회원_아이디);
        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/dislike/{postId}", 게시물_아이디)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", 액세스_토큰)
                        .with(csrf().asHeader())
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("dislike/subtract",
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