package com.monglog.repository;

import com.monglog.domain.Post;
import com.monglog.request.PostSearch;
import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
