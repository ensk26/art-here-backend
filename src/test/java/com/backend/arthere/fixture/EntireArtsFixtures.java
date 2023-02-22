package com.backend.arthere.fixture;

import com.backend.arthere.arts.domain.Address;
import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.arts.domain.Category;
import com.backend.arthere.arts.domain.Location;
import com.backend.arthere.details.domain.Details;
import com.backend.arthere.details.domain.ExhibitionPeriod;
import com.backend.arthere.details.dto.request.ArtRequest;
import com.backend.arthere.details.dto.response.ArtMapResponse;

import java.time.LocalDate;

public class EntireArtsFixtures {

    private static final Long DETAILS_ID = 1L;
    private static final String AUTHOR_NAME = "홍길동";
    private static final String AGENCY = "서울";
    private static final String INFO = "작품 설명";
    private static final boolean STATE = true;
    private static final LocalDate START_DATE = LocalDate.of(2023,02,01);
    private static final LocalDate END_DATE = LocalDate.of(2024,02,01);

    private static final Long ARTS_ID = 2L;
    private static final String ART_NAME = "작품";
    private static final String IMAGE_URL = "이미지경로";
    private static final double LATITUDE = 12.1;
    private static final double LONGITUDE = 12.5;
    private static final String ROAD_ADDRESS = "서울시";
    private static final String OLD_ADDRESS = "서울시 중구";
    private static final Category CATEGORY = Category.CRAFT;
    private static final String CATEGORY_NAME = Category.CRAFT.getCategoryName();

    public static Arts 작품() {
        Location location = new Location(LATITUDE, LONGITUDE);
        Address address = new Address(ROAD_ADDRESS, OLD_ADDRESS);

        return new Arts(ART_NAME, IMAGE_URL, location, address, CATEGORY);
    }

    public static Details 작품_세부정보(final Arts arts) {
        ExhibitionPeriod period = new ExhibitionPeriod(START_DATE, END_DATE);
        return Details.builder()
                .id(DETAILS_ID).authorName(AUTHOR_NAME)
                .agency(AGENCY).info(INFO)
                .state(STATE)
                .period(period)
                .arts(arts).build();
    }

    public static Details 작품_세부정보(final ExhibitionPeriod period) {
        return Details.builder()
                .id(DETAILS_ID).authorName(AUTHOR_NAME)
                .agency(AGENCY).info(INFO)
                .state(STATE)
                .period(period).build();
    }

    public static ArtRequest 작품_저장_요청(final String categoryName) {
        return new ArtRequest(ART_NAME, IMAGE_URL, LATITUDE, LONGITUDE, ROAD_ADDRESS, OLD_ADDRESS,
                categoryName, AUTHOR_NAME, AGENCY, INFO, START_DATE, END_DATE);
    }

    public static ArtRequest 작품_저장_요청() {
        return new ArtRequest(ART_NAME, IMAGE_URL, LATITUDE, LONGITUDE, ROAD_ADDRESS, OLD_ADDRESS,
                CATEGORY_NAME, AUTHOR_NAME, AGENCY, INFO, START_DATE, END_DATE);
    }

    public static ArtMapResponse 작품_맵_조회_응답() {
        return new ArtMapResponse(ARTS_ID, ART_NAME, ROAD_ADDRESS, AUTHOR_NAME, INFO, AGENCY);
    }
}
