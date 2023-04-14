package com.backend.arthere.arts.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ArtImageByLocationRequest {

    @NotNull(message = "latitude를 입력하세요.")
    private Double latitude;

    @NotNull(message = "longitude를 입력하세요.")
    private Double longitude;

    @NotNull(message = "radius를 입력하세요.")
    @Max(value = 50000, message = "radius는 50km가 최대입니다.")
    private Integer radius;

    public void setLatitude(final String latitude) {
        this.latitude = Double.parseDouble(latitude);
    }

    public void setLongitude(final String longitude) {
        this.longitude = Double.parseDouble(longitude);
    }

    public void setRadius(final String radius) {
        this.radius = Integer.parseInt(radius);
    }
}
