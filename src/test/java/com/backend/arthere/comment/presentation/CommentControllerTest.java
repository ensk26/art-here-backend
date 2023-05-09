package com.backend.arthere.comment.presentation;

import com.backend.arthere.comment.dto.request.CommentRequest;
import com.backend.arthere.comment.exception.CommentNotFoundException;
import com.backend.arthere.global.ControllerTest;
import com.backend.arthere.global.TestConfig;
import com.backend.arthere.post.exception.PostNotFoundException;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.backend.arthere.fixture.CommentFixtures.*;
import static com.backend.arthere.fixture.PostFixtures.게시물_아이디;
import static com.backend.arthere.fixture.TokenFixtures.액세스_토큰;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({TestConfig.class})
class CommentControllerTest extends ControllerTest {

    @Test
    @DisplayName("회원은 댓글을 작성한다.")
    @WithMockUser
    public void 댓글_작성() throws Exception {
        //given
        CommentRequest commentRequest = new CommentRequest(댓글_내용);

        given(commentService.save(any(), any(), any()))
                .willReturn(댓글_아이디);
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/posts/{postId}/comments", 게시물_아이디)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", 액세스_토큰)
                        .content(new Gson().toJson(commentRequest))
                        .with(csrf().asHeader())
        );
        //then
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andDo(
                        document("comments/save",
                                requestHeaders(
                                        headerWithName("Authorization")
                                                .description("액세스 토큰")
                                ),
                                pathParameters(
                                        parameterWithName("postId")
                                                .description("게시물 아이디")
                                ),
                                requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("댓글 내용")
                                )
                        ));
    }

    @Test
    @DisplayName("존재하지 않는 게시물의 댓글 작성 요청시 404 에러가 발생한다.")
    @WithMockUser
    public void 존재하지_않는_게시물의_댓글_작성_요청시_에러_발생() throws Exception {
        //given
        CommentRequest commentRequest = new CommentRequest(댓글_내용);

        given(commentService.save(any(), any(), any()))
                .willThrow(new PostNotFoundException());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/posts/{postId}/comments", 게시물_아이디)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", 액세스_토큰)
                        .content(new Gson().toJson(commentRequest))
                        .with(csrf().asHeader())
        );
        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("comments/save/notfound")
                );
    }


    @Test
    @DisplayName("회원은 댓글을 수정한다.")
    @WithMockUser
    public void 댓글_수정() throws Exception {
        //given
        CommentRequest commentRequest = new CommentRequest(댓글_내용_수정);

        doNothing().when(commentService).update(any(), any(), any());

        //when
        ResultActions resultActions = mockMvc.perform(
                put("/api/comments/{commentId}", 게시물_아이디)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", 액세스_토큰)
                        .content(new Gson().toJson(commentRequest))
                        .with(csrf().asHeader())
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("comments/update",
                                requestHeaders(
                                        headerWithName("Authorization")
                                                .description("액세스 토큰")
                                ),
                                pathParameters(
                                        parameterWithName("commentId")
                                                .description("댓글 아이디")
                                ),
                                requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("댓글 내용")
                                )
                        ));
    }

    @Test
    @DisplayName("존재하지 않는 댓글 수정 요청시 404 에러가 발생한다.")
    @WithMockUser
    public void 존재하지_않은_댓글_수정_요청시_에러_발생() throws Exception {
        //given
        CommentRequest commentRequest = new CommentRequest(댓글_내용_수정);

        doThrow(new CommentNotFoundException()).when(commentService).update(any(), any(), any());

        //when
        ResultActions resultActions = mockMvc.perform(
                put("/api/comments/{commentId}", 게시물_아이디)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", 액세스_토큰)
                        .content(new Gson().toJson(commentRequest))
                        .with(csrf().asHeader())
        );
        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("comments/update/notfound")
                );
    }

}