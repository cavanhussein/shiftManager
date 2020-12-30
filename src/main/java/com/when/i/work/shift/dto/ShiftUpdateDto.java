package com.when.i.work.shift.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class ShiftUpdateDto {
    @NotEmpty(message = "startTime cannot be empty or null")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ")
    private Date startTime;
    @NotEmpty(message = "endTime cannot be empty or null")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ")
    private Date endTime;

    public ShiftUpdateDto() {}

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

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
