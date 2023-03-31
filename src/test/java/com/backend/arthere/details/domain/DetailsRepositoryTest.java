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

import java.util.Optional;

import static com.backend.arthere.fixture.ArtsFixtures.작품;
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
    @DisplayName("작품 목록 데이터를 반환한다.")
    public void 작품_목록_데이터_반환() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        Details details1 = detailsRepository.save(작품_세부정보(작품(null), null));
        Details details2 = detailsRepository.save(작품_세부정보(작품(null), null));

        //when
        Page<Details> page = detailsRepository.findDetailsWithArts(pageable);

        //then
        assertAll(
                () -> assertThat(page.getSize()).isEqualTo(2),
                () -> assertThat(page.getTotalElements()).isEqualTo(2),
                () -> assertThat(page.getContent().get(0).getId()).isEqualTo(details2.getId())
        );

    }
}