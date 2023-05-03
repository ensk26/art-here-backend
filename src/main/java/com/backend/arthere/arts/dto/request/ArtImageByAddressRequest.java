package com.backend.arthere.arts.dto.request;

import com.backend.arthere.arts.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@NoArgsConstructor
public class ArtImageByAddressRequest {

    private Long idx;

    @NotBlank(message = "검색어를 입력하세요.")
    private String query;

    @Min(value = 1, message = "limit은 1 이상 10 이하입니다.")
    @Max(value = 10, message = "limit은 1 이상 10 이하입니다.")
    private int limit;

    private Category category;

    public void setIdx(final String idx) {
        this.idx = Long.parseLong(idx);
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public void setLimit(final String limit) {
        this.limit = Integer.parseInt(limit);
    }

    public void setCategory(final String category) {
        this.category = Category.valueOfName(category);
    }
}
