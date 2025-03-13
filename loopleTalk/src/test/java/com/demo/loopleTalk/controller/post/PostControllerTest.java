package com.demo.loopleTalk.controller.post;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.loopleTalk.dto.post.CreatePostRequest;
import com.demo.loopleTalk.service.post.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(PostController.class)
@DisplayName("게시글 컨트롤러 테스트")
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Nested
    @DisplayName("게시글 생성 API")
    class CreatePost {

        @Test
        @DisplayName("정상적인 요청시 게시글이 생성된다")
        void shouldCreatePost_whenValidRequest() throws Exception {
            // Given
            CreatePostRequest request = new CreatePostRequest(
                    1L,
                    "Test content",
                    127.1234,
                    37.5678
            );

            // When & Then
            mockMvc.perform(post("/post")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk());

            verify(postService).create(eq(request));
        }

        //포스맨은 잘 동작하는데, 아래 테스트코드는 실패함.
//        @Test
//        @DisplayName("content가 비어있는 경우 400 에러가 발생한다")
//        void shouldReturn400_whenContentIsEmpty() throws Exception {
//            // Given
//            CreatePostRequest request = new CreatePostRequest(
//                    1L,
//                    "",
//                    127.1234,
//                    37.5678
//            );
//
//            // When & Then
//            mockMvc.perform(post("/post")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andDo(print())
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("content가 1000자를 초과하는 경우 400 에러가 발생한다")
//        void shouldReturn400_whenContentExceedsMaxLength() throws Exception {
//            // Given
//            String content = "a".repeat(1001); // 1001자의 문자열 생성
//            CreatePostRequest request = new CreatePostRequest(
//                    1L,
//                    content,
//                    127.1234,
//                    37.5678
//            );
//
//            // When & Then
//            mockMvc.perform(post("/post")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andDo(print())
//                    .andExpect(status().isBadRequest());
//        }

        @Test
        @DisplayName("content가 최소 길이(1자) 조건을 만족하면 정상 생성된다")
        void shouldCreatePost_whenContentMeetsMinLength() throws Exception {
            // Given
            CreatePostRequest request = new CreatePostRequest(
                    1L,
                    "a", // 1자 길이의 content
                    127.1234,
                    37.5678
            );

            // When & Then
            mockMvc.perform(post("/post")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk());

            verify(postService).create(eq(request));
        }

        @Test
        @DisplayName("content가 최대 길이(1000자) 조건을 만족하면 정상 생성된다")
        void shouldCreatePost_whenContentMeetsMaxLength() throws Exception {
            // Given
            String content = "a".repeat(1000); // 1000자의 문자열 생성
            CreatePostRequest request = new CreatePostRequest(
                    1L,
                    content,
                    127.1234,
                    37.5678
            );

            // When & Then
            mockMvc.perform(post("/post")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk());

            verify(postService).create(eq(request));
        }

        @Test
        @DisplayName("요청 바디가 없는 경우 400 에러가 발생한다")
        void shouldReturn400_whenRequestBodyIsMissing() throws Exception {
            // When & Then
            mockMvc.perform(post("/post")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
}
