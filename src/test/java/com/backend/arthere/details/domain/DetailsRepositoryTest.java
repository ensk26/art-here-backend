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

import java.util.Optional;

import static com.backend.arthere.fixture.EntireArtsFixtures.작품;
import static com.backend.arthere.fixture.EntireArtsFixtures.작품_세부정보;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        Arts arts = artsRepository.save(작품());
        Details details = detailsRepository.save(작품_세부정보(arts));

        //when
        Optional<Details> findDetails = detailsRepository.findByArts(arts);

        //then
        assertThat(findDetails).isNotEmpty();
        assertThat(findDetails.get().getId()).isEqualTo(details.getId());

    }
}