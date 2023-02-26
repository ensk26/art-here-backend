package com.backend.arthere.arts.dto;

import com.backend.arthere.global.validation.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ArtImageByRevisionDateRequest implements Serializable {

    private LocalDateTime revisionDateIdx;

    private Long idx;

    @Min(1)
    @Max(10)
    private Integer limit;

    public void setRevisionDateIdx(@Date String revisionDateIdx) {
        this.revisionDateIdx = LocalDateTime.parse(revisionDateIdx);
    }

    public void setIdx(String idx) {
        this.idx = Long.parseLong(idx);
    }

    public void setLimit(String limit) {
        this.limit = Integer.parseInt(limit);
    }
}
