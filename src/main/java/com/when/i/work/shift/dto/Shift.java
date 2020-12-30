package com.when.i.work.shift.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@JsonIgnoreProperties
@Document(collection = "shift")
public class Shift {
    @Id
    private String id;
    @NotEmpty(message = "startTime cannot be empty or null")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ")
    private Date startTime;
    @NotEmpty(message = "endTime cannot be empty or null")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ")
    private Date endTime;
    @NotEmpty(message = "userId cannot be empty or null")
    private String userId;

    public Shift() {}

    public Shift(String id, Date startTime, Date endTime, String userId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "id='" + id + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", userId='" + userId + '\'' +
                '}';
    }

}
