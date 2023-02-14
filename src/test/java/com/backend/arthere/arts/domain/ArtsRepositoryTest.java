package com.backend.arthere.arts.domain;

import com.backend.arthere.arts.dto.ArtImageResponse;
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
}