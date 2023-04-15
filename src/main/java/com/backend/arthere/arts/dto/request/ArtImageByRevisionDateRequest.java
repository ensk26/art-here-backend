package com.backend.arthere.arts.dto.request;

import com.backend.arthere.arts.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ArtImageByRevisionDateRequest implements Serializable {

    private LocalDateTime date;

    @Min(value = 1, message = "idx는 1이상입니다.")
    private Long idx;

    @Min(value = 1, message = "limit은 1 이상 10 이하입니다.")
    @Max(value = 10, message = "limit은 1 이상 10 이하입니다.")
    @NotNull(message = "limit는 필수 값 입니다.")
    private Integer limit;

    private Category category;

    public void setDate(final String date) {
        this.date = LocalDateTime.parse(date);
    }

    public void setIdx(final String idx) {
        this.idx = Long.parseLong(idx);
    }

    public void setLimit(final String limit) {
        this.limit = Integer.parseInt(limit);
    }

    public void setCategory(final String category) {
            this.category = Category.valueOfName(category);
    }
}
