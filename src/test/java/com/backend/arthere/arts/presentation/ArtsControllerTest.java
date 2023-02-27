package com.backend.arthere.arts.presentation;

import com.backend.arthere.arts.application.ArtsService;
import com.backend.arthere.arts.dto.ArtImageByRevisionDateResponse;
import com.backend.arthere.arts.dto.ArtImageResponse;
import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.global.BaseControllerTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArtsController.class)
class ArtsControllerTest extends BaseControllerTest {

    @MockBean
    private ArtsService artsService;

    @Test
    @WithMockUser
    void 메인화면_이미지_수정일_내림차순_처음_응답() throws Exception {

        //given
        LocalDateTime next = LocalDateTime.parse("2023-01-26T00:09:47.019594");
        boolean hasNext = true;
        ArtImageByRevisionDateResponse response = artsImageResponse(next, hasNext);
        given(artsService.findArtImageByRevisionDate(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media")
                .param("limit", "5"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("image/media",
                                requestParameters(
                                        parameterWithName("limit").description("요청하는 데이터 개수")
                                ),
                                responseFields(
                                        fieldWithPath("artImageResponses.[].id").type(JsonFieldType.NUMBER)
                                                .description("id"),
                                        fieldWithPath("artImageResponses.[].artName").type(JsonFieldType.STRING)
                                                .description("작품 이름"),
                                        fieldWithPath("artImageResponses.[].imageURL").type(JsonFieldType.STRING)
                                                .description("이미지 URL"),
                                        fieldWithPath("nextIdx").type(JsonFieldType.NUMBER)
                                                .description("다음 페이지 idx"),
                                        fieldWithPath("nextRevisionDateIdx").type(JsonFieldType.STRING)
                                                .description("다음 페이지 idx"),
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                                .description("다음 페이지 유무")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void 메인화면_이미지_수정일_내림차순_다음_응답() throws Exception {

        //given
        LocalDateTime next = LocalDateTime.parse("2023-02-26T00:09:47.219594");
        boolean hasNext = true;
        ArtImageByRevisionDateResponse response = artsImageResponse(next, hasNext);
        given(artsService.findArtImageByRevisionDate(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media")
                .param("revisionDateIdx", "2023-02-26T00:09:47.019594")
                .param("idx", "5")
                .param("limit", "5"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("image/media/next",
                                requestParameters(
                                        parameterWithName("revisionDateIdx").description("요청을 시작하는 수정일 위치"),
                                        parameterWithName("idx").description("요청을 시작하는 id 위치"),
                                        parameterWithName("limit").description("요청하는 데이터 개수")
                                ),
                                responseFields(
                                        fieldWithPath("artImageResponses.[].id").type(JsonFieldType.NUMBER)
                                                .description("id"),
                                        fieldWithPath("artImageResponses.[].artName").type(JsonFieldType.STRING)
                                                .description("작품 이름"),
                                        fieldWithPath("artImageResponses.[].imageURL").type(JsonFieldType.STRING)
                                                .description("이미지 URL"),
                                        fieldWithPath("nextIdx").type(JsonFieldType.NUMBER)
                                                .description("다음 페이지 idx"),
                                        fieldWithPath("nextRevisionDateIdx").type(JsonFieldType.STRING)
                                                .description("다음 페이지 idx"),
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                                .description("다음 페이지 유무")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void 메인화면_이미지_수정일_내림차순_데이터_없는_예외_응답() throws Exception {

        //given
        given(artsService.findArtImageByRevisionDate(any())).willThrow(new ArtsNotFoundException());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media")
                .param("limit", "5"));

        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("image/media/notfound")
                );
    }

    //@Test
    //@WithMockUser
    void 메인화면_이미지_수정일_내림차순_요청_limit_1미만_예외_응답() throws Exception {

        //when //then
        Assertions.assertThatThrownBy(() ->
                mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/image/media")
                                .param("limit", "0"))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andDo(
                                document("image/media/min")
                        )).isInstanceOf(Exception.class);
    }

    //@Test
    //@WithMockUser
    void 메인화면_이미지_수정일_내림차순_요청_limit_10초과_예외_응답() throws Exception {

        //when //then
        Assertions.assertThatThrownBy(() ->
                mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/image/media")
                                .param("limit", "11"))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andDo(
                                document("image/media/max")
                        )).isInstanceOf(Exception.class);
    }

    @Test
    @WithMockUser
    void 메인화면_이미지_수정일_내림차순_요청_limit_null_예외_응답() throws Exception {

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media")
                .param("limit", ""));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("image/media/null")
                );
    }

    private ArtImageByRevisionDateResponse artsImageResponse(LocalDateTime next, boolean hasNext) {

        String artName = "모래작품";
        String imageURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand";
        List<ArtImageResponse> responseList = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            responseList.add(new ArtImageResponse((long) i, artName + i,
                    imageURL + i + ".jpg?X-Amz-Algorithm"));
        }

        return new ArtImageByRevisionDateResponse(responseList, 1L, next, hasNext);
    }
}