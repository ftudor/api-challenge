package com.ftudor.error;

import com.fasterxml.jackson.annotation.JsonFormat;

/*
 * Generic API error class
 */

public class ApiError {
    private int code;
    private String status;
    private String message;

    //needed for tests - object mapper readValue
    public ApiError(){
    }

    public ApiError(int code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    @JsonFormat
    public int getCode() {
        return code;
    }

    @JsonFormat
    public String getMessage() {
        return message;
    }

    @JsonFormat
    public String getStatus() {
        return status;
    }
}