package com.when.i.work.shift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ShiftControllerAdvisor {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex, ServletWebRequest request) {
        Map<String, Object> exceptionDetailsMap = createExceptionResponseBody(
                HttpStatus.BAD_REQUEST, ex.getMessage(), "", request.getRequest().getRequestURI());

        return new ResponseEntity<>(exceptionDetailsMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictingShiftException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConflictingShiftException(ConflictingShiftException ex, ServletWebRequest request) {
        Map<String, Object> exceptionDetailsMap = createExceptionResponseBody(
                HttpStatus.BAD_REQUEST, ex.getMessage(), "", request.getRequest().getRequestURI());
        return new ResponseEntity<>(exceptionDetailsMap, HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> createExceptionResponseBody(HttpStatus httpStatus, String error, String message, String path) {
        Map<String, Object> exceptionDetailsMap = new HashMap<>();
        exceptionDetailsMap.put("timestamp", ZonedDateTime.now());
        exceptionDetailsMap.put("status", httpStatus.value());
        exceptionDetailsMap.put("error", error);
        exceptionDetailsMap.put("message", message);
        exceptionDetailsMap.put("path", path);
        return exceptionDetailsMap;
    }
}
