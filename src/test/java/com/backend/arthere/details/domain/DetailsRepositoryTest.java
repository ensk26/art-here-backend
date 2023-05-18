package com.backend.arthere.details.domain;

import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.global.config.JpaConfig;
import com.backend.arthere.global.config.QueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.Optional;

import static com.backend.arthere.fixture.ArtsFixtures.작품;
import static com.backend.arthere.fixture.ArtsFixtures.작품명;
import static com.backend.arthere.fixture.DetailsFixtures.작품_세부정보;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import({QueryDslConfig.class, JpaConfig.class})
class DetailsRepositoryTest {
    @Autowired
    private DetailsRepository detailsRepository;
    @Autowired
    private ArtsRepository artsRepository;
    @Test
    @DisplayName("작품 정보로 세부 정보를 조회한다.")
    public void 작품_정보로_세부정보를_조회() throws Exception {
        //given
        Arts arts = artsRepository.save(작품(null));
        Details details = detailsRepository.save(작품_세부정보(arts, null));

        //when
        Optional<Details> findDetails = detailsRepository.findByArtsId(arts.getId());

        //then

        assertAll(
                () -> assertThat(findDetails).isNotEmpty(),
                () -> assertThat(findDetails.get().getId()).isEqualTo(details.getId())
        );
    }

    @Test
    @DisplayName("최신순으로 작품 목록 데이터를 반환한다.")
    public void 최신순으로_작품_목록_데이터_반환() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Order.desc("revisionDate")));
        Arts arts1 = artsRepository.save(작품(null));
        Arts arts2 = artsRepository.save(작품(null));
        Details details1 = detailsRepository.save(작품_세부정보(arts1, null));
        Details details2 = detailsRepository.save(작품_세부정보(arts2, null));

        //when
        Page<Details> page = detailsRepository.findDetailsWithArts(null, pageable);

        //then
        assertAll(
                () -> assertThat(page.getContent().size()).isEqualTo(2),
                () -> assertThat(page.getTotalElements()).isEqualTo(2),
                () -> assertThat(page.getContent().get(0).getId()).isEqualTo(details2.getId())
        );

    }

    @Test
    @DisplayName("작품명순으로 작품 목록 데이터를 반환한다.")
    public void 작품명순으로_작품_목록_데이터_반환() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Order.asc("artName")));
        Arts arts1 = artsRepository.save(작품(null));
        Arts arts2 = artsRepository.save(작품(null));
        Details details1 = detailsRepository.save(작품_세부정보(arts1, null));
        Details details2 = detailsRepository.save(작품_세부정보(arts2, null));

        //when
        Page<Details> page = detailsRepository.findDetailsWithArts(null, pageable);

        //then
        assertAll(
                () -> assertThat(page.getContent().size()).isEqualTo(2),
                () -> assertThat(page.getTotalElements()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("검색어와 일치하는 작품명이 있을 때 데이터를 작품명순으로 반환한다.")
    public void 검색어와_일치하는_작품명_있을_때_데이터_작품명순_반환() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Order.asc("artName")));
        Arts arts1 = artsRepository.save(작품(null));
        Arts arts2 = artsRepository.save(작품(null));
        Details details1 = detailsRepository.save(작품_세부정보(arts1, null));
        Details details2 = detailsRepository.save(작품_세부정보(arts2, null));

        //when
        Page<Details> page = detailsRepository.findDetailsWithArts(작품명, pageable);

        //then
        assertAll(
                () -> assertThat(page.getContent().size()).isEqualTo(2),
                () -> assertThat(page.getTotalElements()).isEqualTo(2),
                () -> assertThat(page.getContent().get(0).getArts().getArtName()).isEqualTo(작품명)
        );
    }

    @Test
    @DisplayName("검색어와 일치하는 작품명이 없을 때 빈 리스트 반환한다.")
    public void 검색어와_일치하는_작품명_없을_때_빈_리스트_반환() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Order.asc("artName")));
        Arts arts1 = artsRepository.save(작품(null));
        Arts arts2 = artsRepository.save(작품(null));
        Details details1 = detailsRepository.save(작품_세부정보(arts1, null));
        Details details2 = detailsRepository.save(작품_세부정보(arts2, null));
        String notFoundArtName = "없는이름";

        //when
        Page<Details> page = detailsRepository.findDetailsWithArts(notFoundArtName, pageable);

        //then
        assertAll(
                () -> assertThat(page.getContent().size()).isEqualTo(0),
                () -> assertThat(page.getTotalElements()).isEqualTo(0),
                () -> assertThat(page.getTotalPages()).isEqualTo(0)
        );
    }

}