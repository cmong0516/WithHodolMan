package com.monglog.response;


import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;


@Getter
//@JsonInclude(value = Include.NON_EMPTY)
// Json 응답을 할때 빈값 제외 설정.
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String,String> validation;

    @Builder
    public ErrorResponse(String code, String message,Map<String,String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();

    }

    public void addValidation(String field, String defaultMessage) {
        this.validation.put(field, defaultMessage);
    }

}
