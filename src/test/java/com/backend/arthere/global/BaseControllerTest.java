package com.backend.arthere.global;

import com.backend.arthere.auth.presentation.AuthenticationArgumentResolver;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected AuthenticationArgumentResolver authenticationArgumentResolver;
}
