package com.backend.arthere.arts.util;

import com.backend.arthere.arts.dto.ArtImageByLocationRequest;
import com.backend.arthere.arts.dto.ArtImageByLocationResponse;
import com.backend.arthere.arts.dto.LocationRangeResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationUtils {

    private final int EARTH_RADIUS = 6371;

    public LocationRangeResponse getLocationRange(ArtImageByLocationRequest request) {

        double mForLatitude = (1 / (EARTH_RADIUS * 1 * (Math.PI / 180))) / 1000;
        double mForLongitude = (1 / (EARTH_RADIUS * 1 * (Math.PI / 180)
                * Math.cos(Math.toRadians(request.getLatitude())))) / 1000;

        double maxLatitude = request.getLatitude() + (mForLatitude * request.getRadius());
        double minLatitude = request.getLatitude() - (mForLatitude * request.getRadius());
        double maxLongitude = request.getLongitude() + (mForLongitude * request.getRadius());
        double minLongitude = request.getLongitude() - (mForLongitude * request.getRadius());

        return new LocationRangeResponse(maxLatitude, minLatitude, maxLongitude, minLongitude);
    }

    public void removeIncorrectLocation(ArtImageByLocationRequest request,
                                        List<ArtImageByLocationResponse> locationResponses) {

        for (int i = locationResponses.size() - 1; i >= 0; i--) {

            Double latitude = locationResponses.get(i).getLatitude();
            Double longitude = locationResponses.get(i).getLongitude();
            double distance = getDistanceLocation(request.getLatitude(), request.getLongitude(), latitude, longitude);

            if (distance > request.getRadius()) {
                locationResponses.remove(i);
            }
        }
    }

    private double getDistanceLocation(Double centerLatitude, Double centerLongitude,
                                       Double latitude, Double longitude) {

        double diffLatitude = Math.toRadians(latitude - centerLatitude);
        double diffLongitude = Math.toRadians(longitude - centerLongitude);

        double a = Math.sin(diffLatitude / 2) * Math.sin(diffLatitude / 2)
                + Math.cos(Math.toRadians(centerLatitude)) * Math.cos(Math.toRadians(latitude))
                * Math.sin(diffLongitude / 2) * Math.sin(diffLongitude / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c * 1000;  // 단위는 m
    }
}
