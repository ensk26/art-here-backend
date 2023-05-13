package com.backend.arthere.global;


import com.backend.arthere.auth.application.AuthService;
import com.backend.arthere.auth.presentation.AuthController;
import com.backend.arthere.comment.application.CommentService;
import com.backend.arthere.comment.presentation.CommentController;
import com.backend.arthere.dislike.application.DislikeService;
import com.backend.arthere.dislike.presentation.DislikeController;
import com.backend.arthere.like.application.LikeService;
import com.backend.arthere.like.presentation.LikeController;
import com.backend.arthere.member.application.MemberService;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.member.presentation.MemberController;
import com.backend.arthere.post.application.PostService;
import com.backend.arthere.post.presentation.PostController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.backend.arthere.fixture.MemberFixtures.*;
import static org.mockito.BDDMockito.given;
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
@WebMvcTest({AuthController.class, MemberController.class, PostController.class,
            LikeController.class, DislikeController.class, CommentController.class})
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected PostService postService;

    @MockBean
    protected LikeService likeService;

    @MockBean
    protected DislikeService dislikeService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    private MemberRepository memberRepository;


    @BeforeEach
    protected void setUp() throws Exception {

        given(memberRepository.findByEmail(회원_이메일))
                .willReturn(Optional.of(회원(회원_아이디)));

    }

}
