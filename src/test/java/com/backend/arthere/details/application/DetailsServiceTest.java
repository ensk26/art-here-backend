package com.backend.arthere.details.application;

import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.exception.InvalidCategoryException;
import com.backend.arthere.details.domain.Details;
import com.backend.arthere.details.domain.DetailsRepository;
import com.backend.arthere.details.dto.request.ArtRequest;
import com.backend.arthere.details.dto.response.ArtMapResponse;
import com.backend.arthere.details.dto.response.ArtPageResponse;
import com.backend.arthere.details.dto.response.ArtResponse;
import com.backend.arthere.details.dto.response.ArtSaveResponse;
import com.backend.arthere.details.exception.DetailsNotFoundException;
import com.backend.arthere.image.util.PresignedURLUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.backend.arthere.fixture.ArtsFixtures.*;
import static com.backend.arthere.fixture.DetailsFixtures.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DetailsServiceTest {
    @Mock
    private DetailsRepository detailsRepository;

    @Mock
    private ArtsRepository artsRepository;
    @InjectMocks
    private DetailsService detailsService;

    @Mock
    private PresignedURLUtils presignedURLUtils;
    @Test
    @DisplayName("작품 정보를 저장한다.")
    public void 작품_저장() throws Exception {
        //given
        Arts arts = 작품();
        Details details = 작품_세부정보(arts);
        ArtRequest artRequest = 작품_저장_요청();

        given(artsRepository.save(any()))
                .willReturn(arts);
        given(detailsRepository.save(any()))
                .willReturn(details);
        //when
        ArtSaveResponse artSaveResponse = detailsService.save(artRequest);

        //then
        assertThat(artSaveResponse.getArtName()).isEqualTo(arts.getArtName());
    }

    @Test
    @DisplayName("잘못된 카테고리 이름으로 작품 정보 저장하려고 할때 예외가 발생한다.")
    public void 잘못된_카테고리_이름으로_작품_저장시_예외_발생() throws Exception {
        //given
        ArtRequest artRequest = 작품_저장_요청("테스트");

        //when //then
        assertThatThrownBy(() -> detailsService.save(artRequest))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @Test
    @DisplayName("작품 전체 정보를 조회한다.")
    public void 작품_전체_정보_조회() throws Exception {
        //given
        Arts arts = 작품();
        Details details = 작품_세부정보(arts);

        given(detailsRepository.findByArtsId(anyLong()))
                .willReturn(Optional.of(details));

        //when
        ArtResponse artResponse = detailsService.findArt(arts.getId());

        //then
        assertAll(
                () -> assertThat(artResponse.getId()).isEqualTo(details.getId()),
                () -> assertThat(artResponse.getInfo()).isEqualTo(details.getInfo()),
                () -> assertThat(artResponse.getRoadAddress()).isEqualTo(arts.getAddress().getRoadAddress())
        );
    }

    @Test
    @DisplayName("저장되어 있지 않은 arts 아이디로 조회하려고 하면 예외가 발생한다.")
    public void 저장되어_있지_않은_아이디로_작품_전체_조회시_예외_발생() throws Exception {
        //given
        given(detailsRepository.findByArtsId(anyLong()))
                .willThrow(DetailsNotFoundException.class);

        //when //then
        assertThatThrownBy(() -> detailsService.findArt(작품_아이디))
                .isInstanceOf(DetailsNotFoundException.class);
    }

    @Test
    @DisplayName("details에 저장되어 있지 않은 arts로 조회하려고 하면 예외가 발생한다.")
    public void 저장되어_있지_않은_작품으로_작품_전체_조회시_예외_발생() throws Exception {
        //given
        given(detailsRepository.findByArtsId(anyLong()))
                .willThrow(DetailsNotFoundException.class);

        //when //then
        assertThatThrownBy(() -> detailsService.findArt(작품_아이디))
                .isInstanceOf(DetailsNotFoundException.class);
    }

    @Test
    @DisplayName("맵 화면에서 작품 정보를 조회한다.")
    public void 맵_작품_정보_조회() throws Exception {
        //given
        Arts arts = 작품();
        Details details = 작품_세부정보(arts);

        given(detailsRepository.findByArtsId(anyLong()))
                .willReturn(Optional.of(details));

        //when
        ArtMapResponse artMapResponse = detailsService.findArtOnMap(arts.getId());

        //then
        assertAll(
                () -> assertThat(artMapResponse.getAuthorName()).isEqualTo(details.getAuthorName()),
                () -> assertThat(artMapResponse.getInfo()).isEqualTo(details.getInfo()),
                () -> assertThat(artMapResponse.getArtName()).isEqualTo(arts.getArtName()),
                () -> assertThat(artMapResponse.getRoadAddress()).isEqualTo(arts.getAddress().getRoadAddress())
        );
    }

    @Test
    @DisplayName("저장되어 있지 않은 arts 아이디로 맵 화면 작품 조회시 예외가 발생한다.")
    public void 저장되어_있지_않은_아이디로_맵_화면_작품_조회시_예외_발생() throws Exception {
        //given
        given(detailsRepository.findByArtsId(anyLong()))
                .willThrow(DetailsNotFoundException.class);
        //when //then
        assertThatThrownBy(() -> detailsService.findArtOnMap(작품_아이디))
                .isInstanceOf(DetailsNotFoundException.class);
    }

    @Test
    @DisplayName("작품 정보를 수정한다.")
    public void 작품_정보_수정() throws Exception {
        //given
        Arts arts = 작품();
        Details details = 작품_세부정보(arts);
        ArtRequest artRequest = 작품_수정_요청();

        given(detailsRepository.findByArtsId(anyLong()))
                .willReturn(Optional.of(details));

        //when
        detailsService.update(arts.getId(), artRequest);

        //then
        assertAll(
                () -> assertThat(arts.getArtName()).isEqualTo(artRequest.getArtName()),
                () -> assertThat(details.getInfo()).isEqualTo(artRequest.getInfo())
        );
    }

    @Test
    @DisplayName("작품 정보를 삭제한다.")
    public void 작품_정보_삭제() throws Exception {
        //given
        Arts arts = 작품();
        Details details = 작품_세부정보(arts);
        given(detailsRepository.findByArtsId(anyLong()))
                .willReturn(Optional.of(details));
        doNothing().when(detailsRepository).delete(details);
        doNothing().when(artsRepository).delete(arts);
        //when
        detailsService.delete(작품_아이디);
        //then
        assertAll(
                () -> verify(detailsRepository).delete(details),
                () -> verify(detailsRepository).findByArtsId(anyLong())
        );
    }

    @Test
    @DisplayName("관리자가 작품 목록을 조회한다.")
    public void 관리자가_작품_조회() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 3);
        List<Details> content = 관리자_작품_목록();
        Page<Details> page = new PageImpl<>(content, pageable, content.size());

        given(detailsRepository.findDetailsWithArts(any(), any()))
                .willReturn(page);
        given(presignedURLUtils.createImageShareURL(anyString(), any(), any()))
                .willReturn(content.get(0).getArts().getImageURL());
        //when
        ArtPageResponse artPageResponse = detailsService.find(null, pageable);

        //then
        assertAll(
                () -> assertThat(artPageResponse.getTotalPages()).isEqualTo(page.getTotalPages()),
                () -> assertThat(artPageResponse.getTotalElements()).isEqualTo(page.getTotalElements()),
                () -> assertThat(artPageResponse.getArtForAdminResponses().get(0).getId())
                        .isGreaterThan(artPageResponse.getArtForAdminResponses().get(1).getId())
        );
    }

    @Test
    @DisplayName("관리자가 작품 조회시 데이터가 없는 경우 빈 리스트를 반환한다.")
    public void 관리자가_작품_조회시_데이터_없는_경우_빈_리스트_반환() throws Exception {
        //given
        Pageable pageable = PageRequest.of(1, 3);
        List<Details> content = 관리자_작품_목록();
        Page<Details> page = new PageImpl<>(List.of(), pageable, content.size());

        given(detailsRepository.findDetailsWithArts(any(), any()))
                .willReturn(page);
        //when
        ArtPageResponse artPageResponse = detailsService.find(null, pageable);

        //then
        assertAll(
                () -> assertThat(artPageResponse.getTotalPages()).isEqualTo(page.getTotalPages()),
                () -> assertThat(artPageResponse.getTotalElements()).isEqualTo(page.getTotalElements()),
                () -> assertThat(artPageResponse.getArtForAdminResponses().size()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("관리자가 작품명으로 조회한다.")
    public void 관리자_작품명_검색해서_조회() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 3);
        List<Details> content = 관리자_작품_목록();
        Page<Details> page = new PageImpl<>(content, pageable, content.size());

        given(detailsRepository.findDetailsWithArts(any(), any()))
                .willReturn(page);
        given(presignedURLUtils.createImageShareURL(anyString(), any(), any()))
                .willReturn(content.get(0).getArts().getImageURL());
        //when
        ArtPageResponse artPageResponse = detailsService.find(작품명, pageable);

        //then
        assertAll(
                () -> assertThat(artPageResponse.getTotalPages()).isEqualTo(page.getTotalPages()),
                () -> assertThat(artPageResponse.getTotalElements()).isEqualTo(page.getTotalElements()),
                () -> assertThat(artPageResponse.getArtForAdminResponses().get(0).getArtName())
                        .isEqualTo(작품명)
        );
    }
}