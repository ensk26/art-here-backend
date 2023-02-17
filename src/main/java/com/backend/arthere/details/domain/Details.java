package com.backend.arthere.details.domain;

import com.backend.arthere.arts.domain.Arts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
@Table(name = "details")
public class Details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "author_name")
    private String authorName;

    @NotNull
    @Column(name = "agency")
    private String agency;

    @NotNull
    @Column(name = "info", length = 500)
    private String info;

    @NotNull
    @Column(name = "state")
    private boolean state;

    @Embedded
    private ExhibitionPeriod period;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arts_id")
    private Arts arts;

    @Builder
    public Details(final Long id, final String authorName, final String agency, final String info,
                   final boolean state, final ExhibitionPeriod period, final Arts arts) {
        this.id = id;
        this.authorName = authorName;
        this.agency = agency;
        this.info = info;
        this.state = state;
        this.period = period;
        this.arts = arts;
    }

    public void changeState() {

        if(period.getEndDate() == null) {
            state = true;
        }
        else if(period.getEndDate().isBefore(LocalDate.now())) {
            state = false;
        }
        else {
            state = true;
        }
    }

}
