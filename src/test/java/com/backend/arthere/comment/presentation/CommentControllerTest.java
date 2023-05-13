package com.backend.arthere.comment.presentation;

import com.backend.arthere.comment.dto.request.CommentRequest;
import com.backend.arthere.comment.dto.response.CommentPageResponse;
import com.backend.arthere.comment.exception.CommentNotFoundException;
import com.backend.arthere.global.ControllerTest;
import com.backend.arthere.member.exception.NotWriterException;
import com.backend.arthere.post.exception.PostNotFoundException;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    
    @Test
    @DisplayName("게시물에 일치하는 댓글 목록을 조회한다.")
    @WithMockUser
    public void 댓글_조회() throws Exception {
        //given
        CommentPageResponse commentPageResponse = 댓글_목록_응답();

        given(commentService.find(any(), any(Pageable.class), any()))
                .willReturn(commentPageResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/posts/{postId}/comments", 게시물_아이디)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", 액세스_토큰)
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "revisionDate,desc")
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("comments/find",
                                requestHeaders(
                                        headerWithName("Authorization")
                                                .description("액세스 토큰")
                                ),
                                pathParameters(
                                        parameterWithName("postId")
                                                .description("게시물 아이디")
                                ),
                                requestParameters(
                                        parameterWithName("page").description("페이지"),
                                        parameterWithName("size").description("요청 데이터 개수"),
                                        parameterWithName("sort").description("정렬 기준")
                                ),
                                responseFields(
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                                .description("다음 페이지 유무"),
                                        fieldWithPath("commentResponses.[].id").type(JsonFieldType.NUMBER)
                                                .description("댓글 아이디"),
                                        fieldWithPath("commentResponses.[].content").type(JsonFieldType.STRING)
                                                .description("댓글 내용"),
                                        fieldWithPath("commentResponses.[].createDate").type(JsonFieldType.STRING)
                                                .description("생성일 (yyyy-MM-dd'T'HH:mm:ss.nnnnnn)"),
                                        fieldWithPath("commentResponses.[].revisionDate").type(JsonFieldType.STRING)
                                                .description("수정일 (yyyy-MM-dd'T'HH:mm:ss.nnnnnn)"),
                                        fieldWithPath("commentResponses.[].sameWriter").type(JsonFieldType.BOOLEAN)
                                                .description("댓글 작성자인지 여부"),
                                        fieldWithPath("commentResponses.[].writer.id").type(JsonFieldType.NUMBER)
                                                .description("작성자 아이디"),
                                        fieldWithPath("commentResponses.[].writer.name").type(JsonFieldType.STRING)
                                                .description("작성자 이름"),
                                        fieldWithPath("commentResponses.[].writer.profile").type(JsonFieldType.STRING)
                                                .description("작성자 프로필")
                                )
                        ));
    }

    @Test
    @DisplayName("게시물에 해당하는 댓글이 없을 때 빈 데이터를 반환한다.")
    @WithMockUser
    public void 게시물에_일치하는_댓글이_없을_때_빈_데이터_반환() throws Exception {
        //given
        CommentPageResponse commentPageResponse = new CommentPageResponse(false, List.of());

        given(commentService.find(any(), any(Pageable.class), any()))
                .willReturn(commentPageResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/posts/{postId}/comments", 게시물_아이디)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", 액세스_토큰)
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "revisionDate,desc")
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("comments/find/empty",
                                responseFields(
                                        fieldWithPath("commentResponses").type(JsonFieldType.ARRAY)
                                                .description("데이터가 없는 경우 리스트는 비어있음"),
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                                .description("다음 페이지 유무")
                                )
                ));
    }

    @Test
    @DisplayName("댓글을 삭제한다.")
    @WithMockUser
    public void 댓글_삭제() throws Exception {
        //given
        doNothing().when(commentService).delete(any(), any());

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/comments/{commentId}", 게시물_아이디)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", 액세스_토큰)
                        .with(csrf().asHeader())
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("comments/delete",
                                requestHeaders(
                                        headerWithName("Authorization")
                                                .description("액세스 토큰")
                                ),
                                pathParameters(
                                        parameterWithName("commentId")
                                                .description("댓글 아이디")
                                )
                        ));
    }

    @Test
    @DisplayName("작성자가 아닌 회원이 댓글 삭제 요청시 400 에러가 발생한다.")
    @WithMockUser
    public void 작성자가_아닌_회원이_댓글_삭제_요청시_에러_발생() throws Exception {
        //given
        doThrow(new NotWriterException()).when(commentService).delete(any(), any());

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/comments/{commentId}", 게시물_아이디)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", 액세스_토큰)
                        .with(csrf().asHeader())
        );
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("comments/delete/badrequest")
                );
    }
}