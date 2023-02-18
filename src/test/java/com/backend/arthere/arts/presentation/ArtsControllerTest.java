package com.backend.arthere.arts.presentation;

import com.backend.arthere.arts.application.ArtsService;
import com.backend.arthere.arts.dto.ArtImageByLocationResponse;
import com.backend.arthere.arts.dto.ArtImageResponse;
import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.global.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
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
    void 메인화면_이미지_수정일_내림차순_응답() throws Exception {

        //given
        List<ArtImageResponse> response = artsImageResponse();
        given(artsService.findArtImageByRevisionDate(anyLong(), anyLong())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media")
                .param("offset", "1")
                .param("limit", "5"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("image/media",
                                requestParameters(
                                        parameterWithName("offset").description("요청하는 데이터 시작 위치"),
                                        parameterWithName("limit").description("요청하는 데이터 개수")
                                ),
                                responseFields(
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("id"),
                                        fieldWithPath("[].artName").type(JsonFieldType.STRING).description("작품 이름"),
                                        fieldWithPath("[].imageURL").type(JsonFieldType.STRING).description("이미지 URL")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void 메인화면_이미지_수정일_내림차순_데이터_없는_예외_응답() throws Exception {

        //given
        given(artsService.findArtImageByRevisionDate(anyLong(), anyLong())).willThrow(new ArtsNotFoundException());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media")
                .param("offset", "1")
                .param("limit", "5"));

        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("image/media/notfound")
                );
    }

    @Test
    @WithMockUser
    void 지도화면_이미지_중심위치_지정반경_데이터_응답() throws Exception {

        //given
        List<ArtImageByLocationResponse> response = artsImageByLocationResponse();
        given(artsService.findArtImageByLocation(anyDouble(), anyDouble())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/map")
                .param("latitude", "37.587241")
                .param("longitude", "127.019941"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("image/map",
                                requestParameters(
                                        parameterWithName("latitude").description("사용자 위치 위도"),
                                        parameterWithName("longitude").description("사용자 위치 경도")
                                ),
                                responseFields(
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("id"),
                                        fieldWithPath("[].artName").type(JsonFieldType.STRING).description("작품 이름"),
                                        fieldWithPath("[].imageURL").type(JsonFieldType.STRING).description("이미지 URL"),
                                        fieldWithPath("[].latitude").type(JsonFieldType.NUMBER).description("위도"),
                                        fieldWithPath("[].longitude").type(JsonFieldType.NUMBER).description("경도")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void 지도화면_이미지_중심위치_지정반경_데이터_없는_예외_응답() throws Exception {

        //given
        given(artsService.findArtImageByLocation(anyDouble(), anyDouble())).willThrow(new ArtsNotFoundException());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/map")
                .param("latitude", "37.587241")
                .param("longitude", "127.019941"));

        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("image/map/notfound")
                );
    }

    private List<ArtImageResponse> artsImageResponse() {

        String artName = "모래작품";
        String imageURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand";
        List<ArtImageResponse> responseList = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            responseList.add(new ArtImageResponse((long) i, artName + i,
                    imageURL + i + ".jpg?X-Amz-Algorithm"));
        }

        return responseList;
    }

    private List<ArtImageByLocationResponse> artsImageByLocationResponse() {

        String artName = "모래작품";
        String imageURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand";
        Double latitude = 37.58724;
        Double longitude = 127.01994;
        List<ArtImageByLocationResponse> responseList = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            responseList.add(new ArtImageByLocationResponse((long) i, artName + i,
                    imageURL + i + ".jpg?X-Amz-Algorithm", latitude + i, longitude + i));
        }

        return responseList;
    }
}