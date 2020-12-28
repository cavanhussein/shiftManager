package com.when.i.work.shift.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class ShiftUpdateDto {
    @NotEmpty(message = "startTime cannot be empty or null")
    @JsonFormat(pattern="yyyy-MM-dd@HH:mm:ss")
    private Date startTime;
    @NotEmpty(message = "startTime cannot be empty or null")
    @JsonFormat(pattern="yyyy-MM-dd@HH:mm:ss")
    private Date endTime;

    public ShiftUpdateDto(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}
