package com.when.i.work.shift.exception;

import java.util.Date;

public class ConflictingShiftException extends RuntimeException {
    public ConflictingShiftException(Date startTime, Date endTime) {
        super(String.format("Given startTime: %s and endTime: %s conflict with an existing shift.",
                startTime.toString(), endTime.toString()));
    }
}
