package com.backend.arthere;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableJpaAuditing
public class ArtHereApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "/home/ec2-user/app/config/art-here-backend/real-application.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(ArtHereApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}
