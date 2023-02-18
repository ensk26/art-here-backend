package com.backend.arthere.arts.domain;

import com.backend.arthere.arts.dto.ArtImageByLocationResponse;
import com.backend.arthere.arts.dto.ArtImageResponse;
import com.backend.arthere.arts.dto.LocationRangeResponse;
import com.backend.arthere.global.config.JpaConfig;
import com.backend.arthere.global.config.QueryDslConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import({QueryDslConfig.class, JpaConfig.class})
class ArtsRepositoryTest {

    @Autowired
    private ArtsRepository artsRepository;

    @Test
    void 수정일_내림차순으로_id_작품이름_이미지URL_반환() {

        //given
        Long offset = 0L;
        Long limit = 5L;
        artsSaveData();
        ArtImageResponse response = new ArtImageResponse(1L, "모래작품1", "image/sand1");

        //when
        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByRevisionDate(offset, limit);

        ///then
        Assertions.assertThat(artImageResponses.get(4)).usingRecursiveComparison().isEqualTo(response);
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
}