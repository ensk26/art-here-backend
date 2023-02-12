package com.backend.arthere.arts.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Arts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "art_name")
    private String artName;

    @NotNull
    @Column(name = "image_url")
    private String imageURL;

    @NotNull
    private Long latitude;

    @NotNull
    private Long longitude;

    @NotNull
    @Column(name = "load_address")
    private String loadAddress;

    @NotNull
    @Column(name = "old_address")
    private String oldAddress;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @CreatedDate
    @Column(name = "revision_date")
    private LocalDateTime revisionDate;

    @Builder
    public Arts(String artName, String imageURL, Long latitude, Long longitude,
                String loadAddress, String oldAddress, Category category) {

        this.artName = artName;
        this.imageURL = imageURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.loadAddress = loadAddress;
        this.oldAddress = oldAddress;
        this.category = category;
    }
}
