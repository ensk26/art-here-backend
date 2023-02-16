package com.backend.arthere.arts.domain;

import com.backend.arthere.global.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class Arts extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "art_name")
    private String artName;

    @NotNull
    @Column(name = "image_url")
    private String imageURL;

    @Embedded
    private Location location;

    @Embedded
    private Address address;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder
    public Arts(String artName, String imageURL, Location location, Address address, Category category) {
        this.artName = artName;
        this.imageURL = imageURL;
        this.location = location;
        this.address = address;
        this.category = category;
    }
}
