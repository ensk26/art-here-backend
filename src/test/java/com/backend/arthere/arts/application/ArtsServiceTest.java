package com.backend.arthere.arts.application;

import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.dto.*;
import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.arts.util.LocationUtils;
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

import static com.backend.arthere.fixture.ArtsFixtures.메인화면_주소_검색_요청;
import static org.assertj.core.api.Assertions.assertThat;
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
    void 수정일_내림차순으로_데이터_반환() {

        //given
        ArtImageByRevisionDateRequest request = request("1", "2023-02-26T00:09:47.219594", "4");
        String preSignedURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";
        LocalDateTime next = LocalDateTime.parse("2023-01-26T00:09:47.019594");

        given(artsRepository.findArtImageByRevisionDate(any()))
                .willReturn(findArtImageResponse());
        given(artsRepository.findRevisionDateById(anyLong()))
                .willReturn(List.of(next));
        given(presignedURLUtils.createImageShareURL(anyString(), any(), any())).willReturn(preSignedURL);

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
    void 지도화면_이미지_중심위치_지정반경_데이터_반환() {

        //given
        ArtImageByLocationRequest request = artLocationRequest("37.565328", "126.976431", "50");
        String preSignedURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";

        given(locationUtils.getLocationRange(any()))
                .willReturn(locationRangeResponse());
        given(artsRepository.findArtImageByLocation(any()))
                .willReturn(findArtImageByLocationRepositoryResponse());
        given(presignedURLUtils.createImageShareURL(anyString(), any(), any())).willReturn(preSignedURL);

        //when
        List<ArtImageByLocationResponse> responses = artsService.findArtImageByLocation(request);

        //then
        Assertions.assertThat(responses).usingRecursiveFieldByFieldElementComparator()
                .contains(findArtImageByLocationServiceResponse());
    }

    @Test
    void 지도화면_이미지_중심위치_지정반경_null_반환() {

        //given
        ArtImageByLocationRequest request = artLocationRequest("37.565328", "126.976431", "50");
        List<ArtImageByLocationResponse> repositoryResponses = List.of();

        given(locationUtils.getLocationRange(any()))
                .willReturn(locationRangeResponse());
        given(artsRepository.findArtImageByLocation(any()))
                .willReturn(repositoryResponses);

        //when
        List<ArtImageByLocationResponse> responses = artsService.findArtImageByLocation(any());

        // then
        Assertions.assertThat(responses).contains();
    }

    @Test
    void 수정일_내림차순_다음데이터_존재하면_True_반환() {

        //given
        ArtImageByRevisionDateRequest request = request(null, null, "4");
        String preSignedURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";
        LocalDateTime next = LocalDateTime.parse("2023-01-26T00:09:47.019594");

        given(artsRepository.findArtImageByRevisionDate(any()))
                .willReturn(findArtImageResponse());
        given(artsRepository.findRevisionDateById(anyLong()))
                .willReturn(Collections.singletonList(next));
        given(presignedURLUtils.createImageShareURL(anyString(), any(), any())).willReturn(preSignedURL);

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
                .willReturn(findArtImageResponse());
        given(presignedURLUtils.createImageShareURL(anyString(), any(), any())).willReturn(preSignedURL);

        //when
        ArtImageByRevisionDateResponse responses = artsService.findArtImageByRevisionDate(request);

        //then
        Assertions.assertThat(responses.getHasNext()).isEqualTo(false);
    }

    @Test
    public void 주소_검색시_다음_데이터가_존재하면_True_반환() throws Exception {
        //given
        ArtImageByAddressRequest request = 메인화면_주소_검색_요청(null, "서울", "4");
        String preSignedURL = "testPreSignedUrl";

        given(artsRepository.findArtImageByAddress(request))
                .willReturn(findArtImageResponse());
        given(presignedURLUtils.createImageShareURL(anyString(), any(), any())).willReturn(preSignedURL);

        //when
        ArtImageByAddressResponse response = artsService.searchArtImageByAddress(request);

        //then
        assertThat(response.getHasNext()).isTrue();

    }

    @Test
    public void 주소_검색시_다음_데이터가_존재하지_않으면_False_반환() throws Exception {
        //given
        ArtImageByAddressRequest request = 메인화면_주소_검색_요청(null, "서울", "10");
        String preSignedURL = "testPreSignedUrl";

        given(artsRepository.findArtImageByAddress(request))
                .willReturn(findArtImageResponse());
        given(presignedURLUtils.createImageShareURL(anyString(), any(), any())).willReturn(preSignedURL);

        //when
        ArtImageByAddressResponse response = artsService.searchArtImageByAddress(request);

        //then
        assertThat(response.getHasNext()).isFalse();
        assertThat(response.getNextIdx()).isNull();
    }

    @Test
    public void 주소_검색시_검색어에_해당하는_데이터가_존재하지_않으면_False_반환() throws Exception {
        //given
        ArtImageByAddressRequest request = 메인화면_주소_검색_요청(null, "서울", "10");

        given(artsRepository.findArtImageByAddress(request))
                .willReturn(List.of());

        //when
        ArtImageByAddressResponse response = artsService.searchArtImageByAddress(request);

        //then
        assertThat(response.getHasNext()).isFalse();
        assertThat(response.getNextIdx()).isNull();
        assertThat(response.getArtImageResponses()).hasSize(0);
    }

    @Test
    void 작품명_검색_요청_데이터_반환() {

        //given
        ArtImageByArtNameRequest request = artNameRequest("1", "name", "4");
        String preSignedURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";
        LocalDateTime next = LocalDateTime.parse("2023-01-26T00:09:47.019594");

        given(artsRepository.findArtImageByArtName(any()))
                .willReturn(findArtImageResponse());

        given(presignedURLUtils.createImageShareURL(anyString(), any(), any())).willReturn(preSignedURL);

        //when
        ArtImageByArtNameResponse responses = artsService.searchArtImageByArtName(request);

        //then
        Assertions.assertThat(responses.getArtImageResponses()).usingRecursiveFieldByFieldElementComparator()
                .contains(findArtImageByArtNameServiceResponse());
    }

    @Test
    void 작품명_검색_요청_데이터가_없으면_오류_반환() {

        //given
        ArtImageByArtNameRequest request = artNameRequest(null, "name", "5");
        List<ArtImageResponse> repositoryResponses = List.of();

        given(artsRepository.findArtImageByArtName(any()))
                .willReturn(repositoryResponses);

        //when //then
        assertThatThrownBy(() -> artsService.searchArtImageByArtName(request))
                .isInstanceOf(ArtsNotFoundException.class);
    }

    private ArtImageResponse findArtImageByRevisionDateServiceResponse() {

        String artName = "모래작품1";
        String imageURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";

        return new ArtImageResponse(1L, artName, imageURL);
    }

    private ArtImageResponse findArtImageByArtNameServiceResponse() {

        String artName = "모래작품1";
        String imageURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";

        return new ArtImageResponse(1L, artName, imageURL);
    }

    private List<ArtImageResponse> findArtImageResponse() {

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

    private ArtImageByArtNameRequest artNameRequest(String idx, String name, String limit) {
        ArtImageByArtNameRequest request = new ArtImageByArtNameRequest();
        if (idx != null) {
            request.setIdx(idx);
        }
        request.setName(name);
        request.setLimit(limit);

        return request;
    }

    private ArtImageByLocationRequest artLocationRequest(String latitued, String longitude, String radius) {

        ArtImageByLocationRequest request = new ArtImageByLocationRequest();
        request.setLatitude(latitued);
        request.setLongitude(longitude);
        request.setRadius(radius);

        return request;
    }
}