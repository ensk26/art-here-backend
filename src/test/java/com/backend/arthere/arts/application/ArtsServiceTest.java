package com.backend.arthere.arts.application;

import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.dto.ArtImageByRevisionDateRequest;
import com.backend.arthere.arts.dto.ArtImageByRevisionDateResponse;
import com.backend.arthere.arts.dto.ArtImageResponse;
import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.image.util.PresignedURLUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
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
    void 수정일_내림차순으로_데이터_반환() {

        //given
        ArtImageByRevisionDateRequest request = request("1", "2023-02-26T00:09:47.219594", "4");
        String preSignedURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";
        LocalDateTime next = LocalDateTime.parse("2023-01-26T00:09:47.019594");

        given(artsRepository.findArtImageByRevisionDate(any()))
                .willReturn(findArtImageByRevisionDateRepositoryResponse());
        given(artsRepository.findRevisionDateById(anyLong()))
                .willReturn(List.of(next));
        given(presignedURLUtils.createImageShareURL(anyString())).willReturn(preSignedURL);

        //when
        ArtImageByRevisionDateResponse responses = artsService.findArtImageByRevisionDate(request);

        //then
        Assertions.assertThat(responses.getArtImageResponses()).usingRecursiveFieldByFieldElementComparator()
                .contains(findArtImageByRevisionDateServiceResponse());
    }

    @Test
    void 수정일_내림차순으로_데이터가_없으면_오류_반환() {

        //given
        ArtImageByRevisionDateRequest request = request(null, null, "5");
        List<ArtImageResponse> repositoryResponses = List.of();

        given(artsRepository.findArtImageByRevisionDate(any()))
                .willReturn(repositoryResponses);

        //when //then
        assertThatThrownBy(() -> artsService.findArtImageByRevisionDate(request))
                .isInstanceOf(ArtsNotFoundException.class);
    }

    @Test
    void 수정일_내림차순_다음데이터_존재하면_True_반환() {

        //given
        ArtImageByRevisionDateRequest request = request(null, null, "4");
        String preSignedURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";
        LocalDateTime next = LocalDateTime.parse("2023-01-26T00:09:47.019594");

        given(artsRepository.findArtImageByRevisionDate(any()))
                .willReturn(findArtImageByRevisionDateRepositoryResponse());
        given(artsRepository.findRevisionDateById(anyLong()))
                .willReturn(Collections.singletonList(next));
        given(presignedURLUtils.createImageShareURL(anyString())).willReturn(preSignedURL);

        //when
        ArtImageByRevisionDateResponse responses = artsService.findArtImageByRevisionDate(request);

        //then
        Assertions.assertThat(responses.getHasNext()).isEqualTo(true);
    }

    @Test
    void 수정일_내림차순_다음데이터_존재하지_않으면_False_반환() {

        //given
        ArtImageByRevisionDateRequest request = request(null, null, "6");
        String preSignedURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";

        given(artsRepository.findArtImageByRevisionDate(any()))
                .willReturn(findArtImageByRevisionDateRepositoryResponse());
        given(presignedURLUtils.createImageShareURL(anyString())).willReturn(preSignedURL);

        //when
        ArtImageByRevisionDateResponse responses = artsService.findArtImageByRevisionDate(request);

        //then
        Assertions.assertThat(responses.getHasNext()).isEqualTo(false);
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

    private ArtImageByRevisionDateRequest request(String idx, String revisionDateIdx, String limit) {
        ArtImageByRevisionDateRequest request = new ArtImageByRevisionDateRequest();
        if (idx != null) {
            request.setIdx(idx);
        }
        if (revisionDateIdx != null) {
            request.setRevisionDateIdx(revisionDateIdx);
        }
        request.setLimit(limit);

        return request;
    }
}