package com.monglog.request;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@Getter
public class PostCreate {

    @NotBlank
    // validation add
    private String title;
    @NotBlank
    private String content;

    @Builder
    // 클래스 위보단 생성자에 명시해주는게 좋다.
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
