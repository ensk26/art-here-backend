package com.backend.arthere.details.dto.response;

import com.backend.arthere.details.domain.Details;
import com.backend.arthere.arts.domain.Arts;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArtMapResponse {

    private Long id;

    private String artName;

    private String roadAddress;

    private String authorName;

    private String info;

    private String agency;

    public ArtMapResponse(final Details details, final Arts arts) {
        fromDetails(details);
        fromArts(arts);
    }

    private void fromDetails(final Details details) {
        this.authorName = details.getAuthorName();
        this.info = details.getInfo();
        this.agency = details.getAgency();
    }

    private void fromArts(final Arts arts) {
        this.id = arts.getId();
        this.artName = arts.getArtName();
        this.roadAddress = arts.getAddress().getRoadAddress();
    }
}
