package com.monglog.service;

import com.monglog.domain.Post;
import com.monglog.exception.PostNotFound;
import com.monglog.repository.PostRepository;
import com.monglog.request.PostCreate;
import com.monglog.request.PostEdit;
import com.monglog.request.PostSearch;
import com.monglog.response.PostResponse;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        Post post = Post.builder().title(postCreate.getTitle()).content(postCreate.getContent()).build();
        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);

        return PostResponse.builder().id(post.getId()).title(post.getTitle()).content(post.getContent()).build();
    }

//    public List<PostResponse> getList(Pageable pageable) {
//
//        return postRepository.findAll(pageable).stream()
////                .map(post -> PostResponse.builder().id(post.getId()).title(post.getTitle()).content(post.getContent())
////                        .build())
//                // PostResponse 에 Post Entity 를 인자로 받아 객체르 생성해주는 생성자를 설정.
//                .map(PostResponse::new)
//                .collect(Collectors.toList());
//    }

    // QueryDsl.ver
    public List<PostResponse> getList(PostSearch postSearch) {

        return postRepository.getList(postSearch).stream()
//                .map(post -> PostResponse.builder().id(post.getId()).title(post.getTitle()).content(post.getContent())
//                        .build())
                // PostResponse 에 Post Entity 를 인자로 받아 객체르 생성해주는 생성자를 설정.
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFound());

        post.change(postEdit);
        postRepository.save(post);
    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);
        postRepository.delete(post);
    }
}
