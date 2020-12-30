package com.when.i.work.shift.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String reason) {
        super(String.format("Bad request given: %s", reason));
    }
}
