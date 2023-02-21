package com.backend.arthere.arts.application;

import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.dto.ArtImageByLocationResponse;
import com.backend.arthere.arts.dto.ArtImageResponse;
import com.backend.arthere.arts.dto.LocationRangeResponse;
import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.arts.util.LocationUtils;
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

    @Mock
    private LocationUtils locationUtils;

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

    @Test
    void 지도화면_이미지_중심위치_지정반경_데이터_반환() {

        //given
        double latitude = 37.565328;
        double longitude = 126.976431;
        String preSignedURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";

        given(locationUtils.getLocationRange(anyDouble(), anyDouble()))
                .willReturn(locationRangeResponse());
        given(artsRepository.findArtImageByLocation(any()))
                .willReturn(findArtImageByLocationRepositoryResponse());
        given(presignedURLUtils.createImageShareURL(anyString())).willReturn(preSignedURL);

        //when
        List<ArtImageByLocationResponse> responses = artsService.findArtImageByLocation(latitude, longitude);

        //then
        Assertions.assertThat(responses).usingRecursiveFieldByFieldElementComparator()
                .contains(findArtImageByLocationServiceResponse());
    }

    @Test
    void 지도화면_이미지_중심위치_지정반경_null_반환() {

        //given
        double latitude = 37.565328;
        double longitude = 126.976431;
        List<ArtImageByLocationResponse> repositoryResponses = List.of();

        given(locationUtils.getLocationRange(anyDouble(), anyDouble()))
                .willReturn(locationRangeResponse());
        given(artsRepository.findArtImageByLocation(any()))
                .willReturn(repositoryResponses);

        //when //then
        assertThatThrownBy(() -> artsService.findArtImageByLocation(latitude, longitude))
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

    private List<ArtImageByLocationResponse> findArtImageByLocationRepositoryResponse() {

        String artName = "모래작품";
        String imageURL = "image/sand.jpg";
        List<ArtImageByLocationResponse> responseList = new ArrayList<>();
        double latitude = 37.58724;
        double longitude = 127.01994;

        for (int i = 1; i < 6; i++) {
            responseList.add(new ArtImageByLocationResponse((long) i, artName + i,
                    imageURL, latitude, longitude));
        }

        return responseList;
    }

    private LocationRangeResponse locationRangeResponse() {

        return new LocationRangeResponse(37.56577766080296,
                37.564878339197044, 126.97699828171982, 126.9758637182802);
    }

    private ArtImageByLocationResponse findArtImageByLocationServiceResponse() {

        String artName = "모래작품1";
        String imageURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";
        double latitude = 37.58724;
        double longitude = 127.01994;

        return new ArtImageByLocationResponse(1L, artName, imageURL, latitude, longitude);
    }
}