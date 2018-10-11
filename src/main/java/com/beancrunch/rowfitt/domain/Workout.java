package com.beancrunch.rowfitt.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class Workout {

    private String workoutId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;
    private int distance = -1;
    private int timeHh = -1;
    private int timeMm = -1;
    private float timeSss = -1;
    private int splitMm = -1;
    private float splitSss = -1;
    private int strokeRate = -1;
    private int heartRate = -1;
    private String userId;
    private String workoutType;

    public Workout() {
        workoutId = UUID.randomUUID().toString();
    }

    public String toJsonString() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
