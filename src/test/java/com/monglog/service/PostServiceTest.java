package com.monglog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.monglog.domain.Post;
import com.monglog.exception.PostNotFound;
import com.monglog.repository.PostRepository;
import com.monglog.request.PostCreate;
import com.monglog.request.PostEdit;
import com.monglog.request.PostSearch;
import com.monglog.response.PostResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given
        PostCreate postCreate = PostCreate.builder().title("제목입니다.").content("내용입니다.").build();

        //when
        postService.write(postCreate);

        //then
        assertEquals(1L, postRepository.count());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        //given
        Post post = Post.builder().title("foo").content("bar").build();

        postRepository.save(post);

        //when
        PostResponse postResponse = postService.get(post.getId());

        //then
        Assertions.assertNotNull(postResponse);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", post.getTitle());
        assertEquals("bar", post.getContent());

    }

//    @Test
//    @DisplayName("글 여러개 조회")
//    void test3() {
//        //given
//        Post post1 = Post.builder().title("foo1")
//                .content("bar1").build();
//
//        Post post2 = Post.builder().title("foo2")
//                .content("bar2").build();
//
//        postRepository.save(post1);
//        postRepository.save(post2);
//
//
//        //when
//        List<PostResponse> posts = postService.getList();
//
//        //then
//        Assertions.assertEquals(2L, posts.size());
//    }

    @Test
    @DisplayName("글 1 페이지 조회")
    void test3() {
        //given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder().title("MongLog Title " + i).content("호돌맨의 요절복통 개발쇼 " + i).build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

//        Pageable pageable = PageRequest.of(0, 5, by(DESC, "id"));
    // Pageable -> PostSearch

        PostSearch postSearch = PostSearch.builder().page(1).size(10).build();

        //when
        List<PostResponse> posts = postService.getList(postSearch);

        //then
        assertEquals(10L, posts.size());
        assertEquals("MongLog Title 19", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
        //given
        Post post = Post.builder().title("몽").content("수명산파크").build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title("아띠").build();

        //when
        postService.edit(post.getId(),postEdit);

        //then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id = " + post.getId()));

        assertEquals("아띠", changePost.getTitle());
        assertEquals("수명산파크",changePost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() {
        //given
        Post post = Post.builder().title("몽").content("수명산파크").build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().content(null).build();

        //when
        postService.edit(post.getId(),postEdit);

        //then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id = " + post.getId()));

        assertEquals("몽", changePost.getTitle());
        assertEquals("수명산파크",changePost.getContent());
    }

    @Test
    @DisplayName("글 삭제")
    void test6() {
        //given
        Post post = Post.builder().title("몽").content("수명산파크").build();

        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        Assertions.assertEquals(0, postRepository.count());
    }


    @Test
    @DisplayName("글 1개 조회 예외")
    void test7() {
        //given
        Post post = Post.builder().title("foo").content("bar").build();

        postRepository.save(post);

        //when
        //then
        PostNotFound exception = Assertions.assertThrows(PostNotFound.class,
                () -> postService.get(post.getId() + 1L));

        Assertions.assertEquals("존재하지 않는 글입니다.", exception.getMessage());

        // 만약 "존재하지 않는 글입니다." 를 "존재하지 않아요." 라고 바꾸면 테스트 코드도 수정해야한다.
        // 이를 해결하기 위해 Enum 으로 ErrorMessage 를 입력해주는 방법을 사용해보았다.
        // 강의에선 애초에 발생하는 exception 을 IllegalArgumentException 이 아니라 다른 예외를 만들어 처리한다.

    }

    @Test
    @DisplayName("글 삭제 예외")
    void test8() {
       //given
        Post post = Post.builder().title("foo").content("bar").build();
        postRepository.save(post);

        //when
        //then
        Assertions.assertThrows(PostNotFound.class, () -> postService.delete(post.getId()+1L));
    }
}