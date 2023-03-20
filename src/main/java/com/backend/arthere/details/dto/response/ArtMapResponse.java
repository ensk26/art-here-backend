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

    private ArtMapResponse(final Long id, final String artName, final String roadAddress,
                          final String authorName, final String info, final String agency) {
        this.id = id;
        this.artName = artName;
        this.roadAddress = roadAddress;
        this.authorName = authorName;
        this.info = info;
        this.agency = agency;
    }

    public static ArtMapResponse of(final Details details, final Arts arts) {
        return new ArtMapResponse(arts.getId(), arts.getArtName(), arts.getAddress().getRoadAddress(),
                details.getAuthorName(), details.getInfo(), details.getAgency());
    }

}
