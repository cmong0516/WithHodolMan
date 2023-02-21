package com.monglog.controller;

import com.monglog.exception.MongLogException;
import com.monglog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        // 동작확인
//        log.info("ExceptionHandler");
//        log.error("ExceptionHandler error = {}" , e);
        // MethodArgumentNotValidException

//        FieldError fieldError = e.getFieldError();
//        String field = fieldError.getField();
//        String message = fieldError.getDefaultMessage();
//
//        Map<String, String> result = new HashMap<>();
//        result.put(field, message);
//
//        return result;

        ErrorResponse errorResponse = ErrorResponse.builder().code("400").message("잘못된 요청입니다.").build();

        for (FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return errorResponse;
    }

    @ResponseBody
//    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MongLogException.class)
    public ResponseEntity<ErrorResponse> postNotFound(MongLogException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder().code(String.valueOf(statusCode)).message(e.getMessage()).validation(e.getValidation()).build();

        return ResponseEntity.status(statusCode)
                .body(body);


        // ResponseEntity 를 사용하면 status , body 등을 편하게 수정할수 있다.
    }
}
