package com.backend.arthere.arts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class ArtImageByArtNameRequest implements Serializable {

    private Long idx;

    private String artName;

    private Integer limit;

    public void setIdx(final String idx) {
        this.idx = Long.parseLong(idx);
    }

    public void setArtName(final String artName) {
        this.artName = artName;
    }

    public void setLimit(final String limit) {
        this.limit = Integer.parseInt(limit);
    }
}
