package com.monglog.request;

import static java.lang.Math.*;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostSearch {

    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

//    @Builder
//    public PostSearch(Integer page, Integer size) {
//        this.page = page;
//        this.size = size;
//    }

    // @Builder.Default 를 사용하려면 클래스에 @Builder 사용

    public long getOffset() {
        return (long) (max(1,page)- 1) * max(size,MAX_SIZE);
    }
}
