package com.backend.arthere.image.presentation;

import com.backend.arthere.global.BaseControllerTest;
import com.backend.arthere.image.application.ImageService;
import com.backend.arthere.image.dto.ImageResponse;
import com.backend.arthere.image.dto.ImageUploadResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
class ImageControllerTest extends BaseControllerTest {

    @MockBean
    private ImageService imageService;

    @Test
    @WithMockUser
    void 이미지_공유URL_응답() throws Exception {

        //given
        ImageResponse response = imageResponse();
        given(imageService.createImageSharePresignedURL(anyString())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/image/share")
                .param("image", "image/sand.jpg"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("image/share",
                                requestParameters(
                                        parameterWithName("image").description("이미지 경로")
                                ),
                                responseFields(
                                        fieldWithPath("preSignedURL").type(JsonFieldType.STRING).description("인증된 URL")
                                )
                        )
                );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void 이미지_업로드_URL_응답() throws Exception {

        //given
        ImageUploadResponse response = imageUploadResponse();
        given(imageService.createAdminImageUploadPresignedURL()).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/admin/image/upload")
                .header("authorization", "accessToken"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("admin/image/upload",
                                requestHeaders(
                                        headerWithName("authorization").description("관리자 Bearer access token")
                                ),
                                responseFields(
                                        fieldWithPath("preSignedURL").type(JsonFieldType.STRING).description("인증된 URL"),
                                        fieldWithPath("key").type(JsonFieldType.STRING).description("저장할 파일 경로")
                                )
                        )
                );

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void 이미지_삭제_URL_응답() throws Exception {

        //given
        ImageResponse response = imageResponse();
        given(imageService.createAdminDeletePresignedURL(anyString())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/admin/image/delete")
                .param("image", "image/sand.jpg")
                .header("authorization", "accessToken"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("admin/image/delete",
                                requestHeaders(
                                        headerWithName("authorization").description("관리자 Bearer access token")
                                ),
                                requestParameters(
                                        parameterWithName("image").description("이미지 경로")
                                ),
                                responseFields(
                                        fieldWithPath("preSignedURL").type(JsonFieldType.STRING).description("인증된 URL")
                                )
                        )
                );
    }

    @Test
    @WithMockUser(roles = "USER")
    public void 게시물_이미지_업로드_URL_응답() throws Exception {
        //given
        ImageUploadResponse response = imageUploadResponse();
        given(imageService.createUserImageUploadPresignedURL()).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/user/image/upload")
                .header("authorization", "accessToken"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("user/image/upload",
                                requestHeaders(
                                        headerWithName("authorization").description("Bearer access token")
                                ),
                                responseFields(
                                        fieldWithPath("preSignedURL").type(JsonFieldType.STRING).description("인증된 URL"),
                                        fieldWithPath("key").type(JsonFieldType.STRING).description("저장할 파일 경로")
                                )
                        )
                );
    }

    @Test
    @WithMockUser(roles = "USER")
    public void 게시물_이미지_삭제_URL_응답() throws Exception {
        //given
        ImageResponse response = imageResponse();
        given(imageService.createUsernDeletePresignedURL(anyString())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/user/image/delete")
                .param("image", "image/sand.jpg")
                .header("authorization", "accessToken"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("user/image/delete",
                                requestHeaders(
                                        headerWithName("authorization").description("Bearer access token")
                                ),
                                requestParameters(
                                        parameterWithName("image").description("이미지 경로")
                                ),
                                responseFields(
                                        fieldWithPath("preSignedURL").type(JsonFieldType.STRING).description("인증된 URL")
                                )
                        )
                );
    }

    private ImageResponse imageResponse() {

        String url = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";
        return new ImageResponse(url);
    }

    private ImageUploadResponse imageUploadResponse() {

        String url = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";
        String key = "image/sand.jpg";
        return new ImageUploadResponse(url, key);
    }
}