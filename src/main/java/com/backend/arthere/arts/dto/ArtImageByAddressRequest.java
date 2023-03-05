package com.backend.arthere.arts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@NoArgsConstructor
public class ArtImageByAddressRequest implements Serializable {

    private Long idx;

    @NotBlank(message = "검색어를 입력하세요.")
    private String query;

    @Min(value = 1, message = "limit은 1 이상 10 이하입니다.")
    @Max(value = 10, message = "limit은 1 이상 10 이하입니다.")
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
