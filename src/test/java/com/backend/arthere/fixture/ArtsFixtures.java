package com.backend.arthere.fixture;

import com.backend.arthere.arts.domain.Address;
import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.arts.domain.Category;
import com.backend.arthere.arts.domain.Location;
import com.backend.arthere.arts.dto.request.ArtImageByAddressRequest;

public class ArtsFixtures {

    public static final Long 작품_아이디 = 2L;
    public static final String 작품명 = "작품";
    public static final String 작품명_수정 = "작품 수정";
    public static final String 사진 = "이미지경로";
    public static final double 경도 = 12.1;
    public static final double 위도 = 12.5;
    public static final String 도로명_주소 = "서울시";
    public static final Category 카테고리 = Category.CRAFT;
    public static final String 카테고리_명 = Category.CRAFT.getCategoryName();

    public static Arts 작품() {
        Location location = new Location(경도, 위도);
        Address address = new Address(도로명_주소);

        return new Arts(작품_아이디, 작품명, 사진, location, address, 카테고리);
    }

    public static Arts 작품(final Long id) {
        Location location = new Location(경도, 위도);
        Address address = new Address(도로명_주소);

        return new Arts(id, 작품명, 사진, location, address, 카테고리);
    }

    public static ArtImageByAddressRequest 메인화면_주소_검색_요청(final String idx, final String query,
                                                         final String limit) {
        ArtImageByAddressRequest request = new ArtImageByAddressRequest();
        if(idx != null) {
            request.setIdx(idx);
        }
        if(query != null) {
            request.setQuery(query);
        }
        request.setLimit(limit);
        return request;
    }
}
