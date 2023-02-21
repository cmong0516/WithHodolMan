package com.monglog.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public abstract class MongLogException extends RuntimeException{

    public final Map<String, String> validation = new HashMap<>();

    public MongLogException(String message) {
        super(message);
    }

    public MongLogException(String message,Throwable cause) {
        super(message,cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
