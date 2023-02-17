package com.backend.arthere.details.dto.response;

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

    private String oldAddress;

    private String category;

    public ArtResponse(final Details details, final Arts arts) {
        fromDetails(details);
        fromArts(arts);
    }
    private void fromDetails(final Details details) {
        this.id = details.getId();
        this.authorName = details.getAuthorName();
        this.agency = details.getAgency();
        this.info = details.getInfo();
        this.state = details.isState();
        this.startDate = details.getPeriod().getStartDate();
        this.endDate = details.getPeriod().getEndDate();
    }

    private void fromArts(final Arts arts) {
        this.roadAddress = arts.getAddress().getRoadAddress();
        this.oldAddress = arts.getAddress().getOldAddress();
        this.category = arts.getCategory().getCategoryName();
    }

}
