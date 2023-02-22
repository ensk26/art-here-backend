package com.backend.arthere.details.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.backend.arthere.fixture.EntireArtsFixtures.작품_세부정보;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DetailsTest {

    @Test
    @DisplayName("전시 기간이 지났을 때 state를 false로 변경한다.")
    public void 종료된_작품_상태_변경() throws Exception {
        //given
        Details details = 작품_세부정보(new ExhibitionPeriod
                (LocalDate.now(), LocalDate.of(2023,01,01)));
        boolean beforeState = details.isState();

        //when
        details.changeState();

        //then
        assertThat(details.isState()).isEqualTo(false);

    }

}