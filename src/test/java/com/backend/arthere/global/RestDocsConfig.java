package com.backend.arthere.global;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@TestConfiguration
public class RestDocsConfig {
    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        return configurer -> configurer
                .operationPreprocessors()
                .withRequestDefaults(
                        removeHeaders(
                                "X-CSRF-TOKEN"
                        ),
                        prettyPrint())
                .withResponseDefaults(
                        removeHeaders(
                                "X-Content-Type-Options",
                                "X-XSS-Protection",
                                "Cache-Control",
                                "Pragma",
                                "Expires",
                                "X-Frame-Options"
                        ),
                        prettyPrint()
                );
    }
}
