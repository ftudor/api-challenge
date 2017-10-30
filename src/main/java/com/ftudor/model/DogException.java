package com.ftudor.model;

/**
 * App specific Dog exception object
 */
public class DogException extends RuntimeException{
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public DogException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
