package com.ftudor.error;

import com.ftudor.model.DogException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * REST Exception handler
 */


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DogException.class)
    ResponseEntity<?> handleControllerException(DogException ex) {
        HttpStatus status = getStatus(ex.getCode());
        return new ResponseEntity<>(new ApiError(status.value(), status.name(), ex.getMessage()), status);
    }

    private HttpStatus getStatus(int code) {
        return HttpStatus.valueOf(code);
    }
}