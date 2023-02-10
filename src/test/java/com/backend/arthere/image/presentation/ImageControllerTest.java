package com.backend.arthere.image.presentation;

import com.backend.arthere.global.RestDocsConfig;
import com.backend.arthere.image.application.ImageService;
import com.backend.arthere.image.dto.ImageResponse;
import com.backend.arthere.image.dto.ImageUploadResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @MockBean
    private ImageService imageService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void 이미지_공유URL_응답() throws Exception {

        //given
        ImageResponse response = imageResponse();

        given(imageService.createImageSharePresignedURL(anyString())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/image/share")
                .param("image", "image/sand.jpg"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void 이미지_업로드_URL_응답() throws Exception {

        //given
        ImageUploadResponse response = imageUploadResponse();
        given(imageService.createAdminImageUploadPresignedURL()).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/image/upload"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void 이미지_삭제_URL_응답() throws Exception {

        //given
        ImageResponse response = imageResponse();
        given(imageService.createAdminDeletePresignedURL(anyString())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/image/delete")
                .param("image", "image/sand.jpg"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
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