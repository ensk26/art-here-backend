package com.backend.arthere.details.dto.request;


import com.backend.arthere.details.domain.Details;
import com.backend.arthere.details.domain.ExhibitionPeriod;
import com.backend.arthere.arts.domain.Address;
import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.arts.domain.Category;
import com.backend.arthere.arts.domain.Location;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArtRequest {

    @NotBlank(message = "작품명을 입력하세요.")
    private String artName;

    @NotBlank(message = "이미지 경로를 입력하세요.")
    private String imageURL;

    @NotNull(message = "위도를 입력하세요.")
    private Double latitude;

    @NotNull(message = "경도를 입력하세요.")
    private Double longitude;

    @NotBlank(message = "도로명 주소를 입력하세요.")
    private String roadAddress;

    @NotBlank(message = "구주소를 입력하세요.")
    private String oldAddress;

    @NotBlank(message = "카테고리를 입력하세요.")
    private String category;

    @NotBlank(message = "작가 이름을 입력하세요.")
    private String authorName;

    @NotBlank(message = "담당기관을 입력하세요.")
    private String agency;

    @NotBlank(message = "상세 내용을 입력하세요.")
    @Length(max = 255)
    private String info;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;

    public Details toDetails(final Arts arts, final boolean state) {
        ExhibitionPeriod period = new ExhibitionPeriod(startDate, endDate);

        return Details.builder()
                .authorName(authorName)
                .agency(agency)
                .info(info)
                .state(state)
                .period(period)
                .arts(arts).build();
    }

    public Arts toArts() {
        Location location = new Location(latitude, longitude);
        Address address = new Address(roadAddress, oldAddress);

        return Arts.builder()
                .artName(artName)
                .imageURL(imageURL)
                .location(location)
                .address(address)
                .category(Category.valueOfName(category)).build();
    }

}
