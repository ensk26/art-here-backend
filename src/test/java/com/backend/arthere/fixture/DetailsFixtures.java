package com.backend.arthere.fixture;

import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.details.domain.Details;
import com.backend.arthere.details.domain.ExhibitionPeriod;
import com.backend.arthere.details.dto.request.ArtRequest;
import com.backend.arthere.details.dto.response.ArtForAdminResponse;
import com.backend.arthere.details.dto.response.ArtPageResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.backend.arthere.fixture.ArtsFixtures.*;

public class DetailsFixtures {

    public static final Long 작품_세부_아이디 = 1L;
    public static final String 작가 = "홍길동";
    public static final String  담당_기관= "서울";
    public static final String 상세_내용 = "작품 설명";
    public static final String 상세_내용_수정 = "작품 설명 수정";
    public static final boolean 전시중_상태 = true;
    public static final LocalDate 시작_날짜 = LocalDate.of(2022,2,1);
    public static final LocalDate 종료_날짜 = LocalDate.of(2024,2,1);
    public static final LocalDate 지난_종료_날짜 = LocalDate.of(2022,8,1);

    public static Details 작품_세부정보() {
        ExhibitionPeriod period = new ExhibitionPeriod(시작_날짜, 종료_날짜);
        return Details.builder()
                .id(작품_세부_아이디).authorName(작가)
                .agency(담당_기관).info(상세_내용)
                .state(전시중_상태)
                .period(period).build();
    }

    public static Details 작품_세부정보(final Arts arts) {
        return 작품_세부정보(arts, 작품_세부_아이디);
    }

    public static Details 작품_세부정보(final Arts arts, final Long id) {
        ExhibitionPeriod period = new ExhibitionPeriod(시작_날짜, 종료_날짜);
        return new Details(id, 작가, 담당_기관, 상세_내용, 전시중_상태, period, arts);
    }

    public static Details 종료_날짜_지난_작품_세부정보() {
        ExhibitionPeriod period = new ExhibitionPeriod(시작_날짜, 지난_종료_날짜);
        return Details.builder()
                .id(작품_세부_아이디).authorName(작가)
                .agency(담당_기관).info(상세_내용)
                .state(전시중_상태)
                .period(period).build();
    }

    public static ArtRequest 작품_저장_요청() {
        return new ArtRequest(작품명, 사진, 위도, 경도, 도로명_주소, 카테고리_명,
                작가, 담당_기관, 상세_내용, 시작_날짜, 종료_날짜);
    }

    public static ArtRequest 작품_저장_요청(final String categoryName) {
        return new ArtRequest(작품명, 사진, 위도, 경도, 도로명_주소, categoryName,
                작가, 담당_기관, 상세_내용, 시작_날짜, 종료_날짜);
    }

    public static ArtRequest 작품_수정_요청() {
        return new ArtRequest(작품명_수정, 사진, 위도, 경도, 도로명_주소, 카테고리_명,
                작가, 담당_기관, 상세_내용_수정, 시작_날짜, 종료_날짜);
    }

    public static List<Details> 관리자_작품_목록() {
        List<Details> list = new ArrayList<>();
        for (int i = 3; i > 0; i--) {
            Arts arts = 작품(Long.valueOf(i));
            list.add(작품_세부정보(arts, Long.valueOf(i)));
        }
        return list;
    }

    public static ArtPageResponse 작품_목록_응답(final Page<Details> page) {
        List<ArtForAdminResponse> content = new ArrayList<>();
        for(Details details: page.getContent()) {
            ArtForAdminResponse response = ArtForAdminResponse.of(details, details.getArts(), details.getArts().getImageURL());
            response.setDate(LocalDateTime.now(), LocalDateTime.now());
            content.add(response);
        }
        return new ArtPageResponse(page.getTotalElements(), page.getTotalPages(), content);
    }
}
