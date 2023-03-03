package com.backend.arthere.arts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class ArtImageByAddressRequest implements Serializable {

    private Long idx;

    private String query;

    private int limit;

    public void setIdx(final String idx) {
        this.idx = Long.parseLong(idx);
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public void setLimit(final String limit) {
        this.limit = Integer.parseInt(limit);
    }
}
