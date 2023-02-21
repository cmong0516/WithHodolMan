package com.monglog.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monglog.domain.Post;
import com.monglog.repository.PostRepository;
import com.monglog.request.PostCreate;
import com.monglog.request.PostEdit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

//@WebMvcTest
@AutoConfigureMockMvc
@SpringBootTest
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ObjectMapper objectMapper;

//    @Test
//    @DisplayName("/hello 요청시 Hello World 출력")
//    void test() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Hello World"))
//                .andDo(print());
//    }

    // @WebMvcTest 를 붙여주기 전에는 MockMvc 타입의 mockMvc 가 주입되지 않아 Null 이다.
    // MockMvc 는 http 요청에 대한 테스트를 편리하게 해줌.
    // MockMvcResultHandler.print() 를 사용하면 성공시에도 상세 log 를 출력해준다.

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        // 각각 테스트의 독립성을 보장하기 위함.
    }

    @Test
    @DisplayName("/post 요청시 Hello World 출력")
    void test() throws Exception {

        PostCreate request = PostCreate.builder().title("제목입니다.").content("내용입니다.").build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("title", "글 제목입니다.")
//                        .param("content","글 내용입니다")

                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

//    HTTP Method = POST
//    Request URI = /hello
//            Parameters = {title=[글 제목입니다.], content=[글 내용입니다]}
//          Headers = [Content-Type:"application/x-www-form-urlencoded;charset=UTF-8"]
//                  Body = null
//                  Session Attrs = {}

    @Test
    @DisplayName("/post 요청시 title 값은 필수다")
    void test2() throws Exception {
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\" : null, \"content\" : \"내용입니다.\"}")
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("must not be blank"))
                .andDo(print());
    }

    // jsonPath

    @Test
    @DisplayName("/post 요청시 DB에 값이 저장된다")
    void test3() throws Exception {

        PostCreate request = PostCreate.builder().title("제목입니다.").content("내용입니다.").build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        Assertions.assertEquals(1L, postRepository.count());
        // test1 에서 1개 test3 에서 1개
        // 각각 실행시 1개지만 전체 실행시 2개가 된다.
        // @BeforeEach 로 해결.

        Post post = postRepository.findAll().get(0);
        Assertions.assertEquals("제목입니다.", post.getTitle());
        Assertions.assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {

        //given
        Post post = Post.builder().title("foo").content("bar").build();
        postRepository.save(post);

        //when
        //then
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("foo"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {

        //given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder().title("MongLog Title " + i).content("호돌맨의 요절복통 개발쇼 " + i).build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        //when
        //then
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title", is("MongLog Title 19")))
                .andExpect(jsonPath("$[0].content", is("호돌맨의 요절복통 개발쇼 19")))
                .andDo(print());

    }

    @Test
    @DisplayName("글 제목 수정")
    void test6() throws Exception {
        //given
        Post post = Post.builder().title("몽")
                .content("수명산파크")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title("아띠").content("우장산파크")
                .build();

        //when
        //then
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("게시글 삭제")
    void test7() throws Exception {
        //given
        Post post = Post.builder().title("몽").content("수명산파크").build();

        postRepository.save(post);

        //when
        mockMvc.perform(delete("/posts/{postId}", post.getId()).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        //then
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test8() throws Exception {
        //given

        //when
        //then
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}

// Spring Rest Docs

// - 운영코드에 영향 X
// 코드 수정 -> 문서 수정 X
// Test 케이스 실행 -> 문서를 생성해준다.