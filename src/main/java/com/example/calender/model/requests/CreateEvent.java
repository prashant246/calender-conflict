package com.example.calender.model.requests;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateEvent {

    private String name;
    private String creator;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<String> users;
}
