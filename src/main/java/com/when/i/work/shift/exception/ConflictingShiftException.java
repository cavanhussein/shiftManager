package com.when.i.work.shift.exception;

import java.util.Date;

public class ConflictingShiftException extends RuntimeException {
    public ConflictingShiftException(Date startTime, Date endTime) {
        super(String.format("Given startTime: %s is greater than endTime: %s",
                startTime.toString(), endTime.toString()));
    }
}
