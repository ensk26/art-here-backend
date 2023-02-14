package com.backend.arthere.arts.application;

import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.dto.ArtImageResponse;
import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.image.util.PresignedURLUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ArtsServiceTest {

    @InjectMocks
    private ArtsService artsService;

    @Mock
    private ArtsRepository artsRepository;

    @Mock
    private PresignedURLUtils presignedURLUtils;

    @Test
    void 수정일_내림차순으로_id_작품이름_이미지URL_반환() {

        //given
        Long offset = 0L;
        Long limit = 5L;
        String preSignedURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";

        given(artsRepository.findArtImageByRevisionDate(anyLong(), anyLong()))
                .willReturn(findArtImageByRevisionDateRepositoryResponse());
        given(presignedURLUtils.createImageShareURL(anyString())).willReturn(preSignedURL);

        //when
        List<ArtImageResponse> artImageResponses = artsService.findArtImageByRevisionDate(offset, limit);

        //then
        Assertions.assertThat(artImageResponses).usingRecursiveFieldByFieldElementComparator()
                .contains(findArtImageByRevisionDateServiceResponse());
    }

    @Test
    void 수정일_내림차순으로_null_반환() {

        //given
        Long offset = 0L;
        Long limit = 5L;
        List<ArtImageResponse> repositoryResponses = List.of();

        given(artsRepository.findArtImageByRevisionDate(anyLong(), anyLong()))
                .willReturn(repositoryResponses);

        //when //then
        assertThatThrownBy(() -> artsService.findArtImageByRevisionDate(offset, limit))
                .isInstanceOf(ArtsNotFoundException.class);
    }

    private ArtImageResponse findArtImageByRevisionDateServiceResponse() {

        String artName = "모래작품1";
        String imageURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";

        return new ArtImageResponse(1L, artName, imageURL);
    }

    private List<ArtImageResponse> findArtImageByRevisionDateRepositoryResponse() {

        String artName = "모래작품";
        String imageURL = "image/sand.jpg";
        List<ArtImageResponse> responseList = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            responseList.add(new ArtImageResponse((long) i, artName + i,
                    imageURL));
        }

        return responseList;
    }
}