package com.backend.arthere.arts.util;

import com.backend.arthere.arts.dto.ArtImageByLocationResponse;
import com.backend.arthere.arts.dto.LocationRangeResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationUtils {

    private final int EARTH_RADIUS = 6371;
    private final int radius = 50;

    public LocationRangeResponse getLocationRange(Double latitude, Double longitude) {

        double mForLatitude = (1 / (EARTH_RADIUS * 1 * (Math.PI / 180))) / 1000;
        double mForLongitude = (1 / (EARTH_RADIUS * 1 * (Math.PI / 180) * Math.cos(Math.toRadians(latitude)))) / 1000;

        double maxLatitude = latitude + (mForLatitude * radius);
        double minLatitude = latitude - (mForLatitude * radius);
        double maxLongitude = longitude + (mForLongitude * radius);
        double minLongitude = longitude - (mForLongitude * radius);

        return new LocationRangeResponse(maxLatitude, minLatitude, maxLongitude, minLongitude);
    }
}
