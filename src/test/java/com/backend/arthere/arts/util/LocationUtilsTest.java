package com.backend.arthere.arts.util;

import com.backend.arthere.arts.dto.ArtImageByLocationResponse;
import com.backend.arthere.arts.dto.LocationRangeResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class LocationUtilsTest {

    @InjectMocks
    LocationUtils locationUtils;

    @Test
    void 지정_위치에서_최대_최소_위도_경도_반환() {

        //given
        Double latitude = 37.565328;
        Double longitude = 126.976431;
        LocationRangeResponse response = new LocationRangeResponse(37.56577766080296,
                37.564878339197044, 126.97699828171982, 126.9758637182802);

        //when
        LocationRangeResponse locationRangeResponse = locationUtils.getLocationRange(latitude, longitude);

        //then
        Assertions.assertThat(locationRangeResponse).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void 두좌표_거리가_만족하는_좌표만_반환() {

        //given
        Double centerlatitude = 37.565328;
        Double centerlongitude = 126.976431;
        List<ArtImageByLocationResponse> locationResponses = testPositionProvider();

        //when
        locationUtils.removeIncorrectLocation(centerlatitude, centerlongitude, locationResponses);

        //then
        Assertions.assertThat(locationResponses).hasSize(4);
    }

    private List<ArtImageByLocationResponse> testPositionProvider() {

        List<ArtImageByLocationResponse> locationResponses = new ArrayList<>();
        locationResponses.add(new ArtImageByLocationResponse(1L, "name", "url",
                37.56577766080296, 126.976431));
        locationResponses.add(new ArtImageByLocationResponse(2L, "name", "url",
                37.564878339197044, 126.976431));
        locationResponses.add(new ArtImageByLocationResponse(3L, "name", "url",
                37.565328, 126.97699828171982));
        locationResponses.add(new ArtImageByLocationResponse(4L, "name", "url",
                37.565328, 126.9758637182802));
        locationResponses.add(new ArtImageByLocationResponse(5L, "name", "url",
                37.56577766080296, 126.9758637182802));

        return locationResponses;
    }
}