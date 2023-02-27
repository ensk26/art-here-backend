package com.backend.arthere.arts.domain;

import com.backend.arthere.arts.dto.ArtImageByRevisionDateRequest;
import com.backend.arthere.arts.dto.ArtImageResponse;
import com.backend.arthere.global.config.JpaConfig;
import com.backend.arthere.global.config.QueryDslConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

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

    private void artsSaveData() {

        String artName = "모래작품";
        String imageURL = "image/sand";
        Location location = new Location(1.22, 1.33);
        Address address = new Address("loadAddress", "oldAddress");

        for (int i = 1; i < 6; i++) {
            artsRepository.save(new Arts(artName + i, imageURL + i, location,
                    address, Category.PICTURE));
        }
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