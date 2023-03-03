package com.backend.arthere.arts.domain;

import com.backend.arthere.arts.dto.*;
import com.backend.arthere.global.config.JpaConfig;
import com.backend.arthere.global.config.QueryDslConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.backend.arthere.fixture.EntireArtsFixtures.메인화면_주소_검색_요청;
import static com.backend.arthere.fixture.EntireArtsFixtures.작품;
import static java.lang.Thread.sleep;

@DataJpaTest
@Import({QueryDslConfig.class, JpaConfig.class})
class ArtsRepositoryTest {

    @Autowired
    private ArtsRepository artsRepository;

    @Test
    void 수정일_내림차순으로_데이터_반환() {

        //given
        ArtImageByRevisionDateRequest request = request(null, null, "5");
        artsSaveData();

        //when
        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByRevisionDate(request);

        ///then
        Assertions.assertThat(artImageResponses.get(4).getArtName()).isEqualTo("모래작품1");
    }

    @Test
    void 시작_수정일보다_미만인_데이터_반환() throws InterruptedException {

        //given
        artsSaveData();
        sleep(100);
        LocalDateTime now = LocalDateTime.now();
        String dateIdx = now.toString();
        ArtImageByRevisionDateRequest request = request("1", dateIdx, "5");

        //when
        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByRevisionDate(request);

        ///then
        Long id = artImageResponses.get(0).getId();
        List<LocalDateTime> date = artsRepository.findRevisionDateById(id);
        LocalDateTime next = date.get(0);

        Assertions.assertThat(next).isBefore(now);
    }

    @Test
    void 시작_수정일보다_미만이고_시간이_같은_데이터_조건_확인() {

        //given
        artsSaveData();
        ArtImageByRevisionDateRequest request = request(null, null, "1");

        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByRevisionDate(request);

        Long preId = artImageResponses.get(0).getId();
        List<LocalDateTime> preDates = artsRepository.findRevisionDateById(preId);
        LocalDateTime preDate = preDates.get(0);

        ArtImageByRevisionDateRequest nextRequest = request(preId.toString(), preDate.toString(), "1");

        //when
        List<ArtImageResponse> responses = artsRepository.findArtImageByRevisionDate(nextRequest);

        ///then
        Long nextId = responses.get(0).getId();
        List<LocalDateTime> nextDates = artsRepository.findRevisionDateById(nextId);
        LocalDateTime nextDate = nextDates.get(0);

        if (nextDate.isEqual(preDate)) {
            Assertions.assertThat(nextId).isLessThan(preId);
        } else {
            Assertions.assertThat(nextDate).isBefore(preDate);
        }
    }

    @Test
    void 지도화면_이미지_중심위치_지정반경_데이터_반환() {

        //given
        artsSaveData();

        //when
        List<ArtImageByLocationResponse> responses = artsRepository.findArtImageByLocation(locationRangeResponse());

        ///then
        Assertions.assertThat(responses.size()).isEqualTo(4);
    }

    @Test
    public void 검색어와_일치하는_도로명_주소가_있을_때_데이터_반환() throws Exception {
        //given
        artsSaveData();
        String query = "load";
        ArtImageByAddressRequest request = 메인화면_주소_검색_요청(null, query, "1");

        //when
        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByAddress(request);

        Optional<Arts> arts = artsRepository.findById(artImageResponses.get(0).getId());

        //then
        Assertions.assertThat(arts.get().getAddress().getRoadAddress()).contains(query);
    }

    @Test
    public void 검색어와_일치하는_도로명_주소가_없을_때_빈_리스트_반환() throws Exception {
        //given
        artsSaveData();
        ArtImageByAddressRequest request = 메인화면_주소_검색_요청(null, "test", "5");

        //when
        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByAddress(request);

        //then
        Assertions.assertThat(artImageResponses.size()).isEqualTo(0);
    }
    
    @Test
    public void 입력한_아이디_미만이고_검색어와_일치하는_데이터_반환() throws Exception {
        //given
        Arts arts = 작품();
        artsRepository.save(arts);
        String idx= String.valueOf(arts.getId() + 1);

        ArtImageByAddressRequest request = 메인화면_주소_검색_요청(idx, arts.getAddress().getRoadAddress(), "1");

        //when
        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByAddress(request);

        //then
        Assertions.assertThat(artImageResponses.get(0).getId()).isLessThan(Long.parseLong(idx));
    }

    private void artsSaveData() {

        String artName = "모래작품";
        String imageURL = "image/sand";
        Address address = new Address("loadAddress", "oldAddress");

        for (int i = 1; i < 6; i++) {
            artsRepository.save(new Arts(artName + i, imageURL + i,
                    new Location(37.564878339197044 + (0.0002 * i), 126.9758637182802 + (0.0002 * i)),
                    address, Category.PICTURE));
        }
    }

    private LocationRangeResponse locationRangeResponse() {

        return new LocationRangeResponse(37.56577766080296,
                37.564878339197044, 126.97699828171982, 126.9758637182802);
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