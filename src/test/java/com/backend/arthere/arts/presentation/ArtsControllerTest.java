package com.backend.arthere.arts.presentation;

import com.backend.arthere.arts.application.ArtsService;
import com.backend.arthere.arts.dto.*;
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
        ArtImageByRevisionDateResponse response = artsImageResponse(1L, next, hasNext);
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
        ArtImageByRevisionDateResponse response = artsImageResponse(1L, next, hasNext);
        given(artsService.findArtImageByRevisionDate(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media")
                .param("date", "2023-02-26T00:09:47.019594")
                .param("idx", "5")
                .param("limit", "5"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("image/media/next",
                                requestParameters(
                                        parameterWithName("date").description("요청을 시작하는 수정일 위치"),
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

    @Test
    @WithMockUser
    void 지도화면_이미지_중심위치_지정반경_데이터_응답() throws Exception {

        //given
        List<ArtImageByLocationResponse> response = artsImageByLocationResponse();
        given(artsService.findArtImageByLocation(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/map")
                .param("latitude", "37.587241")
                .param("longitude", "127.019941")
                .param("radius", "50"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("image/map",
                                requestParameters(
                                        parameterWithName("latitude").description("사용자 위치 위도"),
                                        parameterWithName("longitude").description("사용자 위치 경도"),
                                        parameterWithName("radius").description("반경")
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
    void 지도화면_이미지_중심위치_지정반경_데이터_없음() throws Exception {

        //given
        given(artsService.findArtImageByLocation(any())).willReturn(List.of());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/map")
                .param("latitude", "37.587241")
                .param("longitude", "127.019941")
                .param("radius", "50"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("image/map/empty",
                                requestParameters(
                                        parameterWithName("latitude").description("사용자 위치 위도"),
                                        parameterWithName("longitude").description("사용자 위치 경도"),
                                        parameterWithName("radius").description("반경")
                                ),
                                responseFields(
                                )
                        )
                );
    }


    @Test
    @WithMockUser
    void 메인화면_이미지_수정일_내림차순_다음_데이터가_없는_경우() throws Exception {

        //given
        ArtImageByRevisionDateResponse response = artsImageResponse(null, null, false);
        given(artsService.findArtImageByRevisionDate(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media")
                .param("date", "2023-02-26T00:09:47.019594")
                .param("idx", "5")
                .param("limit", "5"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("image/media/false")

                );
    }

    @Test
    @WithMockUser
    void 메인화면_이미지_수정일_내림차순_요청_limit_1미만_예외_응답() throws Exception {

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media")
                .param("limit", "0"));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("image/media/min")
                );
    }

    @Test
    @WithMockUser
    void 메인화면_이미지_수정일_내림차순_요청_limit_10초과_예외_응답() throws Exception {

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media")
                .param("limit", "11"));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("image/media/max")
                );
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

    @Test
    @WithMockUser
    public void 메인화면에서_주소_검색시_처음_응답() throws Exception {
        //given
        ArtImageByAddressResponse response = artImageByAddressResponse(true, 3L);

        given(artsService.searchArtImageByAddress(any()))
                .willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media/address")
                .param("query", "test")
                .param("limit", "5"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("image/media/address",
                                requestParameters(
                                        parameterWithName("query").description("검색어"),
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
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                                .description("다음 페이지 유무")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    public void 메인화면에서_주소_검색시_다음_응답() throws Exception {
        //given
        ArtImageByAddressResponse response = artImageByAddressResponse(false, null);

        given(artsService.searchArtImageByAddress(any()))
                .willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media/address")
                .param("idx", "3")
                .param("query", "test")
                .param("limit", "5"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("image/media/address/next",
                                requestParameters(
                                        parameterWithName("idx").description("요청을 시작하는 id 위치"),
                                        parameterWithName("query").description("검색어"),
                                        parameterWithName("limit").description("요청하는 데이터 개수")
                                ),
                                responseFields(
                                        fieldWithPath("artImageResponses.[].id").type(JsonFieldType.NUMBER)
                                                .description("id"),
                                        fieldWithPath("artImageResponses.[].artName").type(JsonFieldType.STRING)
                                                .description("작품 이름"),
                                        fieldWithPath("artImageResponses.[].imageURL").type(JsonFieldType.STRING)
                                                .description("이미지 URL"),
                                        fieldWithPath("nextIdx").type(JsonFieldType.NULL)
                                                .description("다음 페이지 idx"),
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                                .description("다음 페이지 유무")
                                )
                        )
                );

    }

    @Test
    @WithMockUser
    public void 메인화면에서_주소_검색시_검색어와_일치하는_데이터가_없는_경우_응답() throws Exception {
        //given
        ArtImageByAddressResponse response = new ArtImageByAddressResponse(List.of(), false, null);
        given(artsService.searchArtImageByAddress(any()))
                .willReturn(response);
        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media/address")
                .param("idx", "3")
                .param("query", "not Found")
                .param("limit", "5"));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("image/media/address/empty",
                                requestParameters(
                                        parameterWithName("idx").description("요청을 시작하는 id 위치"),
                                        parameterWithName("query").description("검색어"),
                                        parameterWithName("limit").description("요청하는 데이터 개수")
                                ),
                                responseFields(
                                        fieldWithPath("artImageResponses").type(JsonFieldType.ARRAY)
                                                .description("데이터가 없는 경우 리스트는 비어있음"),
                                        fieldWithPath("nextIdx").type(JsonFieldType.NULL)
                                                .description("다음 페이지 idx"),
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                                .description("다음 페이지 유무")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    public void 메인화면에서_주소_검색시_검색어를_입력하지_않은_경우_에러_발생() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media/address")
                .param("limit", "5"));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("image/media/address/notFound")
                );
    }

    @Test
    @WithMockUser
    public void 메인화면에서_주소_검색시_limit가_1_미만_10_초과시_에러_발생() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media/address")
                .param("query", "test")
                .param("limit", "12"));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("image/media/address/invalid")
                );
    }

    @Test
    @WithMockUser
    public void 메인화면에서_작품명_검색시_처음_응답() throws Exception {

        //given
        ArtImageByArtNameResponse response = artImageByArtNameResponse(true, 3L);

        given(artsService.searchArtImageByArtName(any()))
                .willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media/name")
                .param("name", "name")
                .param("limit", "5"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("image/media/name",
                                requestParameters(
                                        parameterWithName("name").description("작품명"),
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
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                                .description("다음 페이지 유무")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    public void 메인화면에서_작품명_검색시_다음_응답() throws Exception {

        //given
        ArtImageByArtNameResponse response = artImageByArtNameResponse(false, null);

        given(artsService.searchArtImageByArtName(any()))
                .willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media/name")
                .param("idx", "3")
                .param("name", "name")
                .param("limit", "5"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("image/media/name/next",
                                requestParameters(
                                        parameterWithName("idx").description("요청을 시작하는 id 위치"),
                                        parameterWithName("name").description("검색 이름"),
                                        parameterWithName("limit").description("요청하는 데이터 개수")
                                ),
                                responseFields(
                                        fieldWithPath("artImageResponses.[].id").type(JsonFieldType.NUMBER)
                                                .description("id"),
                                        fieldWithPath("artImageResponses.[].artName").type(JsonFieldType.STRING)
                                                .description("작품 이름"),
                                        fieldWithPath("artImageResponses.[].imageURL").type(JsonFieldType.STRING)
                                                .description("이미지 URL"),
                                        fieldWithPath("nextIdx").type(JsonFieldType.NULL)
                                                .description("다음 페이지 idx"),
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                                .description("다음 페이지 유무")
                                )
                        )
                );

    }

    @Test
    @WithMockUser
    public void 메인화면에서_작품명_검색시_검색어와_일치하는_데이터가_없는_경우_응답() throws Exception {

        //given
        given(artsService.searchArtImageByArtName(any())).willThrow(new ArtsNotFoundException());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media/name")
                .param("idx", "3")
                .param("name", "name")
                .param("limit", "5"));
        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("image/media/name/notfound")
                );
    }

    @Test
    @WithMockUser
    void 메인화면에서_작품명_요청시_limit_1미만_예외_응답() throws Exception {

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media/name")
                .param("name", "name")
                .param("limit", "0"));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("image/media/name/min")
                );
    }

    @Test
    @WithMockUser
    void 메인화면에서_작품명_요청시_limit_10초과_예외_응답() throws Exception {

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media/name")
                .param("name", "name")
                .param("limit", "11"));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("image/media/name/max")
                );
    }

    @Test
    @WithMockUser
    void 메인화면에서_작품명_요청시_name_null_예외_응답() throws Exception {

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/media/name")
                .param("name", "")
                .param("limit", "5"));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("image/media/name/null")
                );
    }

    private ArtImageByAddressResponse artImageByAddressResponse(boolean hasNext, Long nextIdx) {
        String artName = "모래작품";
        String imageURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand";
        List<ArtImageResponse> responses = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            responses.add(new ArtImageResponse((long) i, artName + i,
                    imageURL + i + ".jpg?X-Amz-Algorithm"));
        }
        return new ArtImageByAddressResponse(responses, hasNext, nextIdx);
    }

    private ArtImageByArtNameResponse artImageByArtNameResponse(boolean hasNext, Long nextIdx) {
        String artName = "모래작품";
        String imageURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand";
        List<ArtImageResponse> responses = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            responses.add(new ArtImageResponse((long) i, artName + i,
                    imageURL + i + ".jpg?X-Amz-Algorithm"));
        }
        return new ArtImageByArtNameResponse(responses, nextIdx, hasNext);
    }

    private ArtImageByRevisionDateResponse artsImageResponse(Long nextIdx, LocalDateTime next, boolean hasNext) {

        String artName = "모래작품";
        String imageURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand";
        List<ArtImageResponse> responseList = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            responseList.add(new ArtImageResponse((long) i, artName + i,
                    imageURL + i + ".jpg?X-Amz-Algorithm"));
        }

        return new ArtImageByRevisionDateResponse(responseList, nextIdx, next, hasNext);
    }

    private List<ArtImageByLocationResponse> artsImageByLocationResponse() {

        String artName = "모래작품";
        String imageURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand";
        double latitude = 37.58724;
        double longitude = 127.01994;
        List<ArtImageByLocationResponse> responseList = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            responseList.add(new ArtImageByLocationResponse((long) i, artName + i,
                    imageURL + i + ".jpg?X-Amz-Algorithm", latitude + i, longitude + i));
        }

        return responseList;
    }
}