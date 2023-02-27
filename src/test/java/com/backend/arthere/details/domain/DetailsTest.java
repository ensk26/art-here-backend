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
    
    @Test
    @DisplayName("작가 이름을 수정한다.")
    public void 작가_이름_수정() throws Exception {
        //given
        String updateAuthorName = "작가 변경";
        Details details = Details.builder()
                .authorName("작가").build();
        //when
        details.updateAuthorName(updateAuthorName);
        //then
        assertThat(details.getAuthorName()).isEqualTo(updateAuthorName);
    }

    @Test
    @DisplayName("담당 기관을 수정한다.")
    public void 담당_기관_수정() throws Exception {
        //given
        String updateAgency = "담당 기관 변경";
        Details details = Details.builder()
                .agency("담당 기관").build();
        //when
        details.updateAgency(updateAgency);
        //then
        assertThat(details.getAgency()).isEqualTo(updateAgency);
    }

    @Test
    @DisplayName("작품 상세 내용을 수정한다.")
    public void 작품_상세_내용_수정() throws Exception {
        //given
        String updateInfo = "작품 상세 내용 수정";
        Details details = Details.builder()
                .info("상세 내용").build();
        //when
        details.updateInfo(updateInfo);
        //then
        assertThat(details.getInfo()).isEqualTo(updateInfo);
    }

    @Test
    @DisplayName("작품 상태를 수정한다.")
    public void 작품_상태_수정() throws Exception {
        //given
        Details details = Details.builder()
                .state(false).build();
        //when
        details.updateState(true);
        //then
        assertThat(details.isState()).isEqualTo(true);
    }

    @Test
    @DisplayName("작품 전시 기간을 수정한다.")
    public void 작품_전시_기간_수정() throws Exception {
        ExhibitionPeriod period = new ExhibitionPeriod(LocalDate.now(),
                LocalDate.of(2025,01,01));
        //given
        Details details = Details.builder()
                .period(new ExhibitionPeriod(LocalDate.of(2024,01,01), null))
                .build();
        //when
        details.updatePeriod(period);
        //then
        assertThat(details.getPeriod().getStartDate()).isEqualTo(period.getStartDate());
        assertThat(details.getPeriod().getEndDate()).isEqualTo(period.getEndDate());
    }

}