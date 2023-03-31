package com.backend.arthere.details.dto.response;

import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.details.domain.Details;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ArtForAdminResponse {

    private Long id;

    private String artName;

    private String imageURL;

    private String authorName;

    private String agency;

    private String info;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double latitude;

    private Double longitude;

    private String roadAddress;

    private String category;

    private LocalDateTime createDate;

    private LocalDateTime revisionDate;

    private ArtForAdminResponse(final Long id, final String artName, final String imageURL,
                                final String authorName, final String agency, final String info,
                               final LocalDate startDate, final LocalDate endDate,
                               final Double latitude, final Double longitude, final String roadAddress,
                                final String category, final LocalDateTime createDate, final LocalDateTime revisionDate) {
        this.id = id;
        this.artName = artName;
        this.imageURL = imageURL;
        this.authorName = authorName;
        this.agency = agency;
        this.info = info;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.roadAddress = roadAddress;
        this.category = category;
        this.createDate = createDate;
        this.revisionDate = revisionDate;
    }

    public static ArtForAdminResponse of(final Details details, final Arts arts, final String presignedURL) {
        return new ArtForAdminResponse(arts.getId(), arts.getArtName(), presignedURL,
                details.getAuthorName(), details.getAgency(),
                details.getInfo(), details.getPeriod().getStartDate(), details.getPeriod().getEndDate(),
                arts.getLocation().getLatitude(), arts.getLocation().getLongitude(), arts.getAddress().getRoadAddress(),
                arts.getCategory().getCategoryName(), arts.getCreateDate(), arts.getRevisionDate());
    }

    public void setDate(final LocalDateTime createDate, final LocalDateTime revisionDate) {
        this.createDate = createDate;
        this.revisionDate = revisionDate;
    }
}
