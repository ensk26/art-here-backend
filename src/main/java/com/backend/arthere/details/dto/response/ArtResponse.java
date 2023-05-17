package com.backend.arthere.details.dto.response;

import com.backend.arthere.arts.domain.Location;
import com.backend.arthere.details.domain.Details;
import com.backend.arthere.arts.domain.Arts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ArtResponse {

    private Long id;

    private String authorName;

    private String agency;

    private String info;

    private boolean state;

    private LocalDate startDate;

    private LocalDate endDate;

    private String roadAddress;

    private String category;

    private Location location;

    private ArtResponse(final Long id, final String authorName, final String agency, final String info,
                       final boolean state, final LocalDate startDate, final LocalDate endDate,
                       final String roadAddress, final String category, final Location location) {
        this.id = id;
        this.authorName = authorName;
        this.agency = agency;
        this.info = info;
        this.state = state;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roadAddress = roadAddress;
        this.category = category;
        this.location = location;
    }

    public static ArtResponse of(final Details details, final Arts arts) {
        return new ArtResponse(details.getId(), details.getAuthorName(), details.getAgency(),
                details.getInfo(), details.isState(), details.getPeriod().getStartDate(),
                details.getPeriod().getEndDate(), arts.getAddress().getRoadAddress(),
                arts.getCategory().getCategoryName(), arts.getLocation());
    }

}
