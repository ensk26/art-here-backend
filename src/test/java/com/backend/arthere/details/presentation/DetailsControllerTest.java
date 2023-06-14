package com.backend.arthere.details.presentation;

import com.backend.arthere.details.application.DetailsService;
import com.backend.arthere.details.domain.Details;
import com.backend.arthere.details.dto.request.ArtRequest;
import com.backend.arthere.details.dto.response.ArtMapResponse;
import com.backend.arthere.details.dto.response.ArtPageResponse;
import com.backend.arthere.details.dto.response.ArtResponse;
import com.backend.arthere.details.dto.response.ArtSaveResponse;
import com.backend.arthere.details.exception.DetailsNotFoundException;
import com.backend.arthere.global.BaseControllerTest;
import com.backend.arthere.global.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.backend.arthere.fixture.ArtsFixtures.*;
import static com.backend.arthere.fixture.DetailsFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({TestConfig.class})
@WebMvcTest(DetailsController.class)
class DetailsControllerTest extends BaseControllerTest {
    @MockBean
    protected DetailsService detailsService;
    @Autowired
    private ObjectMapper objectMapper;
    private final String accessToken = "Bearer testAccess";

    @Test
    @DisplayName("관리자는 작품 정보를 저장한다.")
    @WithMockUser(roles = "ADMIN")
    public void 관리자가_작품_정보_저장() throws Exception {
        //given
        ArtRequest artRequest = 작품_저장_요청();
        ArtSaveResponse artSaveResponse = new ArtSaveResponse(작품_아이디, artRequest.getArtName());

        given(detailsService.save(any()))
                .willReturn(artSaveResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(artRequest))
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/admin/art",
                                requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰")
                                ),
                                requestFields(
                                        fieldWithPath("artName").type(JsonFieldType.STRING)
                                                .description("작품명 (50자 이하)"),
                                        fieldWithPath("imageURL").type(JsonFieldType.STRING)
                                                .description("이미지 경로"),
                                        fieldWithPath("latitude").type(JsonFieldType.NUMBER)
                                                .description("위도"),
                                        fieldWithPath("longitude").type(JsonFieldType.NUMBER)
                                                .description("경도"),
                                        fieldWithPath("roadAddress").type(JsonFieldType.STRING)
                                                .description("도로명 주소"),
                                        fieldWithPath("category").type(JsonFieldType.STRING)
                                                .description("카테고리 (사진, 벽화, 공예, 조각, 그림, 설치미술, 키네틱아트, " +
                                                        "반응형 미디어, 조명예술, 기타)"),
                                        fieldWithPath("authorName").type(JsonFieldType.STRING)
                                                .description("작가 이름 (50자 이하, 필수값 아님)"),
                                        fieldWithPath("agency").type(JsonFieldType.STRING)
                                                .description("담당기관 (50자 이하)"),
                                        fieldWithPath("info").type(JsonFieldType.STRING)
                                                .description("상세 내용 (255자 이하)"),
                                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                                .description("시작일 (yyyy-MM-dd)"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                                .description("종료일 (종료일은 필수값이 아닙니다.) (yyyy-MM-dd)")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                                .description("작품 아이디"),
                                        fieldWithPath("artName").type(JsonFieldType.STRING)
                                                .description("작품명")
                                )
                        ));
    }

    @Test
    @DisplayName("회원이 작품 정보를 저장하려고 할 때 403 에러가 발생한다.")
    @WithMockUser(roles = "USER")
    public void 회원이_작품_정보_저장_요청시_에러_발생() throws Exception {
        //given
        ArtRequest artRequest = 작품_저장_요청();

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(artRequest))
        );
        //then
        resultActions.andExpect(status().isForbidden())
                .andDo(print())
                .andDo(
                        document("api/admin/art/forbidden")
                );
    }

    @Test
    @DisplayName("일부 데이터를 입력하지 않고 작품 저장을 요청하면 400 에러가 발생한다.")
    @WithMockUser(roles = "ADMIN")
    public void 일부_데이터를_입력하지_않고_작품_저장_요청시_에러_발생() throws Exception {
        //given
        ArtRequest artRequest = new ArtRequest();

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(artRequest))
        );
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("api/admin/art/invalid")
                );
    }

    @Test
    @DisplayName("작품 전체 정보를 조회한다.")
    @WithMockUser
    public void 작품_전체_정보_조회() throws Exception {
        //given
        ArtResponse artResponse = ArtResponse.of(작품_세부정보(), 작품(), "이미지");

        given(detailsService.findArt(2L))
                .willReturn(artResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "2")
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/art",
                                requestParameters(
                                        parameterWithName("id").description("작품 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                                .description("작품 세부 정보 아이디 (작품 아이디와 다름)"),
                                        fieldWithPath("artName").type(JsonFieldType.STRING)
                                                .description("작품 이름"),
                                        fieldWithPath("roadAddress").type(JsonFieldType.STRING)
                                                .description("도로명 주소"),
                                        fieldWithPath("category").type(JsonFieldType.STRING)
                                                .description("카테고리 (사진, 벽화, 공예, 조각, 그림, 설치미술, 키네틱아트, " +
                                                        "반응형 미디어, 조명예술, 기타)"),
                                        fieldWithPath("authorName").type(JsonFieldType.STRING)
                                                .description("작가 이름"),
                                        fieldWithPath("agency").type(JsonFieldType.STRING)
                                                .description("담당기관"),
                                        fieldWithPath("info").type(JsonFieldType.STRING)
                                                .description("상세 내용"),
                                        fieldWithPath("state").type(JsonFieldType.BOOLEAN)
                                                .description("상태 (전시 중인지 여부)"),
                                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                                .description("시작일 (yyyy-MM-dd)"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                                .description("종료일 (yyyy-MM-dd)"),
                                        fieldWithPath("location.latitude").type(JsonFieldType.NUMBER)
                                                .description("위도"),
                                        fieldWithPath("location.longitude").type(JsonFieldType.NUMBER)
                                                .description("경도"),
                                        fieldWithPath("imageURL").type(JsonFieldType.STRING)
                                                .description("이미지 경로")
                                )
                        ));
    }

    @Test
    @DisplayName("저장되어 있지 않은 arts 아이디로 작품 전체 조회시 404 에러가 발생한다.")
    @WithMockUser
    public void 저장되어_있지_않은_작품으로_작품_전체_조회시_에러_발생() throws Exception {
        //given
        given(detailsService.findArt(2L))
                .willThrow(new DetailsNotFoundException());

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "2")
        );
        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("api/art/notFound")
                );
    }

    @Test
    @DisplayName("올바르지 않은 arts 아이디 형식으로 작품 전체 조회시 400 에러가 발생한다.")
    @WithMockUser
    public void 올바르지_않은_아이디_형식으로_작품_전체_조회시_에러_발생() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "haha")
        );
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("api/art/mismatch")
                );
    }

    @Test
    @DisplayName("맵 화면에서 작품 정보를 조회한다.")
    @WithMockUser
    public void 맵_화면에서_작품_정보_조회() throws Exception {
        //given
        ArtMapResponse artMapResponse = ArtMapResponse.of(작품_세부정보(), 작품());

        given(detailsService.findArtOnMap(2L))
                .willReturn(artMapResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/art/map")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "2")
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/art/map",
                                requestParameters(
                                        parameterWithName("id").description("작품 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                                .description("작품 아이디"),
                                        fieldWithPath("artName").type(JsonFieldType.STRING)
                                                .description("작품명"),
                                        fieldWithPath("roadAddress").type(JsonFieldType.STRING)
                                                .description("도로명 주소"),
                                        fieldWithPath("authorName").type(JsonFieldType.STRING)
                                                .description("작가 이름"),
                                        fieldWithPath("agency").type(JsonFieldType.STRING)
                                                .description("담당기관"),
                                        fieldWithPath("info").type(JsonFieldType.STRING)
                                                .description("상세 내용")
                                )
                        ));
    }

    @Test
    @DisplayName("저장되어 있지 않은 arts 아이디로 맵 화면 작품 조회시 404 에러가 발생한다.")
    @WithMockUser
    public void 저장되어_있지_않은_작품으로_맵_화면_작품_조회시_에러_발생() throws Exception {
        //given
        given(detailsService.findArtOnMap(2L))
                .willThrow(new DetailsNotFoundException());
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/art/map")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "2")
        );
        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("api/art/map/notFound")
                );
    }

    @Test
    @DisplayName("관리자가 작품 정보를 수정한다.")
    @WithMockUser(roles = "ADMIN")
    public void 관리자가_작품_정보_수정() throws Exception {
        //given
        ArtRequest artRequest = 작품_수정_요청();
        doNothing().when(detailsService).update(2L, artRequest);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .param("id", "2")
                        .content(objectMapper.writeValueAsString(artRequest))
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/admin/art/update",
                                requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰")
                                ),
                                requestParameters(
                                        parameterWithName("id").description("작품 아이디")
                                ),
                                requestFields(
                                        fieldWithPath("artName").type(JsonFieldType.STRING)
                                                .description("작품명 (50자 이하)"),
                                        fieldWithPath("imageURL").type(JsonFieldType.STRING)
                                                .description("이미지 경로"),
                                        fieldWithPath("latitude").type(JsonFieldType.NUMBER)
                                                .description("위도"),
                                        fieldWithPath("longitude").type(JsonFieldType.NUMBER)
                                                .description("경도"),
                                        fieldWithPath("roadAddress").type(JsonFieldType.STRING)
                                                .description("도로명 주소"),
                                        fieldWithPath("category").type(JsonFieldType.STRING)
                                                .description("카테고리 (사진, 벽화, 공예, 조각, 그림, 설치미술, 키네틱아트, " +
                                                        "반응형 미디어, 조명예술, 기타)"),
                                        fieldWithPath("authorName").type(JsonFieldType.STRING)
                                                .description("작가 이름 (50자 이하, 필수값 아님)"),
                                        fieldWithPath("agency").type(JsonFieldType.STRING)
                                                .description("담당기관 (50자 이하)"),
                                        fieldWithPath("info").type(JsonFieldType.STRING)
                                                .description("상세 내용 (255자 이하)"),
                                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                                .description("시작일 (yyyy-MM-dd)"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                                .description("종료일 (종료일은 필수값이 아닙니다.) (yyyy-MM-dd)")
                                )
                        ));
    }

    @Test
    @DisplayName("회원이 작품 정보를 수정하려고 할 때 403 에러가 발생한다.")
    @WithMockUser(roles = "USER")
    public void 회원이_작품_정보_수정_요청시_에러_발생() throws Exception {
        //given
        ArtRequest artRequest = 작품_수정_요청();
        //when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .param("id", "2")
                        .content(objectMapper.writeValueAsString(artRequest))
        );
        //then
        resultActions.andExpect(status().isForbidden())
                .andDo(print())
                .andDo(
                        document("api/admin/art/update/forbidden")
                );
    }

    @Test
    @DisplayName("관리자가 작품 정보를 삭제한다.")
    @WithMockUser(roles = "ADMIN")
    public void 관리자가_작품_정보_삭제() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .param("id", "2")
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/admin/art/delete",
                                requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰")
                                ),
                                requestParameters(
                                        parameterWithName("id").description("작품 아이디")
                                )
                        )
                );
    }

    @Test
    @DisplayName("관리자가 저장되어 있지 않은 작품 삭제 요청시 404 에러가 발생한다.")
    @WithMockUser(roles = "ADMIN")
    public void 저장되어_있지_않은_작품_삭제_요청시_에러_발생() throws Exception {
        //given
        doThrow(new DetailsNotFoundException()).when(detailsService).delete(2L);
        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .param("id", "2")
        );
        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("api/admin/art/delete/notFound")
                );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void 관리자가_최신순_아이디_내림차순으로_작품_정보_조회() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Order.desc("revisionDate"),
                Sort.Order.desc("id")));
        List<Details> content = 관리자_작품_목록();
        Page<Details> page = new PageImpl<>(content, pageable, content.size());

        given(detailsService.find(any(), any(Pageable.class)))
                .willReturn(작품_목록_응답(page));
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "revisionDate,desc", "id,desc")
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/admin/art/find",
                                requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰")
                                ),
                                requestParameters(
                                        parameterWithName("page").description("페이지"),
                                        parameterWithName("size").description("요청 데이터 개수"),
                                        parameterWithName("sort").description("정렬 기준")
                                ),
                                responseFields(
                                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER)
                                                .description("전체 데이터 개수"),
                                        fieldWithPath("totalPages").type(JsonFieldType.NUMBER)
                                                .description("전체 페이지 개수"),
                                        fieldWithPath("artForAdminResponses.[].id").type(JsonFieldType.NUMBER)
                                                .description("작품 아이디"),
                                        fieldWithPath("artForAdminResponses.[].artName").type(JsonFieldType.STRING)
                                                .description("작품명"),
                                        fieldWithPath("artForAdminResponses.[].imageURL").type(JsonFieldType.STRING)
                                                .description("이미지 경로"),
                                        fieldWithPath("artForAdminResponses.[].latitude").type(JsonFieldType.NUMBER)
                                                .description("위도"),
                                        fieldWithPath("artForAdminResponses.[].longitude").type(JsonFieldType.NUMBER)
                                                .description("경도"),
                                        fieldWithPath("artForAdminResponses.[].roadAddress").type(JsonFieldType.STRING)
                                                .description("도로명 주소"),
                                        fieldWithPath("artForAdminResponses.[].category").type(JsonFieldType.STRING)
                                                .description("카테고리 (사진, 벽화, 공예, 조각, 그림, 설치미술, 키네틱아트, " +
                                                        "반응형 미디어, 조명예술, 기타)"),
                                        fieldWithPath("artForAdminResponses.[].authorName").type(JsonFieldType.STRING)
                                                .description("작가 이름"),
                                        fieldWithPath("artForAdminResponses.[].agency").type(JsonFieldType.STRING)
                                                .description("담당기관"),
                                        fieldWithPath("artForAdminResponses.[].info").type(JsonFieldType.STRING)
                                                .description("상세 내용"),
                                        fieldWithPath("artForAdminResponses.[].startDate").type(JsonFieldType.STRING)
                                                .description("시작일 (yyyy-MM-dd)"),
                                        fieldWithPath("artForAdminResponses.[].endDate").type(JsonFieldType.STRING)
                                                .description("종료일 (yyyy-MM-dd)"),
                                        fieldWithPath("artForAdminResponses.[].createDate").type(JsonFieldType.STRING)
                                                .description("생성일 (yyyy-MM-dd'T'HH:mm:ss.nnnnnn)"),
                                        fieldWithPath("artForAdminResponses.[].revisionDate").type(JsonFieldType.STRING)
                                                .description("수정일 (yyyy-MM-dd'T'HH:mm:ss.nnnnnn)")
                                )
                        ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void 관리자가_검색어와_일치하는_작품명이_있을_때_작품명순_으로_작품_정보_조회() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Order.asc("artName")));
        List<Details> content = 관리자_작품_목록();
        Page<Details> page = new PageImpl<>(content, pageable, content.size());

        given(detailsService.find(작품명, pageable))
                .willReturn(작품_목록_응답(page));
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "artName")
                        .param("name", 작품명)
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/admin/art/search",
                                requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰")
                                ),
                                requestParameters(
                                        parameterWithName("page").description("페이지"),
                                        parameterWithName("size").description("요청 데이터 개수"),
                                        parameterWithName("sort").description("정렬 기준"),
                                        parameterWithName("name").description("검색어")
                                ),
                                responseFields(
                                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER)
                                                .description("전체 데이터 개수"),
                                        fieldWithPath("totalPages").type(JsonFieldType.NUMBER)
                                                .description("전체 페이지 개수"),
                                        fieldWithPath("artForAdminResponses.[].id").type(JsonFieldType.NUMBER)
                                                .description("작품 아이디"),
                                        fieldWithPath("artForAdminResponses.[].artName").type(JsonFieldType.STRING)
                                                .description("작품명"),
                                        fieldWithPath("artForAdminResponses.[].imageURL").type(JsonFieldType.STRING)
                                                .description("이미지 경로"),
                                        fieldWithPath("artForAdminResponses.[].latitude").type(JsonFieldType.NUMBER)
                                                .description("위도"),
                                        fieldWithPath("artForAdminResponses.[].longitude").type(JsonFieldType.NUMBER)
                                                .description("경도"),
                                        fieldWithPath("artForAdminResponses.[].roadAddress").type(JsonFieldType.STRING)
                                                .description("도로명 주소"),
                                        fieldWithPath("artForAdminResponses.[].category").type(JsonFieldType.STRING)
                                                .description("카테고리 (사진, 벽화, 공예, 조각, 그림, 설치미술, 키네틱아트, " +
                                                        "반응형 미디어, 조명예술, 기타)"),
                                        fieldWithPath("artForAdminResponses.[].authorName").type(JsonFieldType.STRING)
                                                .description("작가 이름"),
                                        fieldWithPath("artForAdminResponses.[].agency").type(JsonFieldType.STRING)
                                                .description("담당기관"),
                                        fieldWithPath("artForAdminResponses.[].info").type(JsonFieldType.STRING)
                                                .description("상세 내용"),
                                        fieldWithPath("artForAdminResponses.[].startDate").type(JsonFieldType.STRING)
                                                .description("시작일 (yyyy-MM-dd)"),
                                        fieldWithPath("artForAdminResponses.[].endDate").type(JsonFieldType.STRING)
                                                .description("종료일 (yyyy-MM-dd)"),
                                        fieldWithPath("artForAdminResponses.[].createDate").type(JsonFieldType.STRING)
                                                .description("생성일 (yyyy-MM-dd'T'HH:mm:ss.nnnnnn)"),
                                        fieldWithPath("artForAdminResponses.[].revisionDate").type(JsonFieldType.STRING)
                                                .description("수정일 (yyyy-MM-dd'T'HH:mm:ss.nnnnnn)")
                                )
                        ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void 관리자가_작품_조회시_데이터가_없는_경우_응답() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Order.desc("id")));
        ArtPageResponse artPageResponse = new ArtPageResponse(0L, 0, List.of());
        given(detailsService.find(null, pageable))
                .willReturn(artPageResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .param("page", "0")
                        .param("size", "3")
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/admin/art/find/empty",
                                responseFields(
                                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER)
                                                .description("전체 데이터 개수"),
                                        fieldWithPath("totalPages").type(JsonFieldType.NUMBER)
                                                .description("전체 페이지 개수"),
                                        fieldWithPath("artForAdminResponses").type(JsonFieldType.ARRAY)
                                                .description("데이터가 없는 경우 리스트는 비어있음")
                                )
                        ));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void 회원이_작품_정보_조회_요청시_에러_발생() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .param("page", "0")
                        .param("size", "5")
        );
        //then
        resultActions.andExpect(status().isForbidden())
                .andDo(print())
                .andDo(
                        document("api/admin/art/find/forbidden")
                );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void 페이지_혹은_사이즈를_잘못_입력했을_때_에러_발생() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .param("page", "0")
                        .param("size", "12")
        );
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("api/admin/art/find/invalid")
                );
    }
}