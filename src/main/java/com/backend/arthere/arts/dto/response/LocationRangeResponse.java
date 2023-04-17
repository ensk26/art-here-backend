package com.backend.arthere.arts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationRangeResponse {

    private Double maxLatitude;

    private Double minLatitude;

    private Double maxLongitude;

    private Double minLongitude;
}
