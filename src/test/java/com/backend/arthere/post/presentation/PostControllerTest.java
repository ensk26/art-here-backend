package com.backend.arthere.post.presentation;

import com.backend.arthere.global.ControllerTest;
import com.backend.arthere.post.dto.response.PostResponse;
import com.backend.arthere.post.exception.PostNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.backend.arthere.fixture.PostFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PostControllerTest extends ControllerTest {

    @Test
    @DisplayName("id로 게시물을 조회한다.")
    @WithMockUser
    public void 게시물_조회() throws Exception {
        //given
        PostResponse postResponse = 게시물_응답();

        given(postService.find(any(), any()))
                .willReturn(postResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/posts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(게시물_아이디))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                   document("posts/find",
                           requestParameters(
                                   parameterWithName("id").description("게시물 아이디")
                           ),
                           responseFields(
                                   fieldWithPath("content").type(JsonFieldType.STRING)
                                           .description("게시물 내용"),
                                   fieldWithPath("likeCount").type(JsonFieldType.NUMBER)
                                           .description("좋아요 개수"),
                                   fieldWithPath("unlikeCount").type(JsonFieldType.NUMBER)
                                           .description("싫어요 개수"),
                                   fieldWithPath("createDate").type(JsonFieldType.STRING)
                                           .description("생성일"),
                                   fieldWithPath("revisionDate").type(JsonFieldType.STRING)
                                           .description("수정일"),
                                   fieldWithPath("likeState").type(JsonFieldType.BOOLEAN)
                                           .description("좋아요 눌렀는지 여부"),
                                   fieldWithPath("writer.id").type(JsonFieldType.NUMBER)
                                           .description("작성자 아이디"),
                                   fieldWithPath("writer.name").type(JsonFieldType.STRING)
                                           .description("작성자 이름"),
                                   fieldWithPath("writer.profile").type(JsonFieldType.STRING)
                                           .description("작성자 프로필")


                           )

                   ));
    }

    @Test
    @DisplayName("id에 해당하는 게시물이 존재하지 않을 때 404 에러가 발생한다.")
    @WithMockUser
    public void 게시물이_존재하지_않을때_에러_발생() throws Exception {
        //given
        given(postService.find(any(), any()))
                .willThrow(new PostNotFoundException());
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/posts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(게시물_아이디))
        );
        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("posts/find/notFound")
                );
    }


}