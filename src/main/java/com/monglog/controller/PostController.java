package com.monglog.controller;

import com.monglog.request.PostCreate;
import com.monglog.request.PostEdit;
import com.monglog.request.PostSearch;
import com.monglog.response.PostResponse;
import com.monglog.service.PostService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    // HttpMethod
    // GET, POST , PUT , PATCH , DELETE , OPTIONS, HEAD , TRACE, CONNECT


//    @GetMapping("/hello")
//    public String get() {
//        return "Hello World";
//    }

//    @PostMapping("/hello")
//    public String post(@RequestParam String title, @RequestParam String content) {
//        log.info("title = {} , content = {}", title, content);
//        return "Hello World";
//    }

//    @PostMapping("/hello")
//    public String post(@RequestParam Map<String,String> params) {
//        log.info("params = {}" , params);
//        String title = params.get("title");
//        String content = params.get("content");
//
//        log.info("title = {} , content = {}" ,title,content);
//        return "Hello World";
//    }

// 2023-02-14 11:58:37.115  INFO 25281 --- [    Test worker] com.monglog.controller.PostController    : params = {title=글 제목입니다., content=글 내용입니다}
// 2023-02-14 11:58:37.117  INFO 25281 --- [    Test worker] com.monglog.controller.PostController    : title = 글 제목입니다. , content = 글 내용입니다

//    @PostMapping("/hello")
//    public String post(@ModelAttribute PostCreate params) {
//        log.info("params = {}" , params.toString());
//        return "Hello World";
//    }

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request){
//        log.info("params = {}" , params.toString());
//
//        String title = params.getTitle();
//        String content = params.getContent();

//        if (title == null && title.equals("")) {
//            throw new Exception("타이틀 값이 없어요");
//        }

        // @RequestBody 객체의 필드가 늘어나면 그만크 if 문을 작성해줘야한다.
        // 따라서 validation 의 @Valid 를 사용하자.
        // 컨트롤러로 넘어오기 전에 에러 발생.


        // 아래 부분은 Controller
//        if (result.hasErrors()) {
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            FieldError firstFieldError = fieldErrors.get(0);
//            String invalidFiledName = firstFieldError.getField();
//            String errorMessage = firstFieldError.getDefaultMessage();
//
//            Map<String, String> error = new HashMap<>();
//
//            error.put(invalidFiledName, errorMessage);
//
//            return error;
//        }

        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
        // entity 를 반환하는것은 후에 여러 문제를 일으킬수 있다고 배웠다.
        // 아마 후에 querydsl 파트가 있던데 이때 dto 로 변환할거같다.
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }

}
