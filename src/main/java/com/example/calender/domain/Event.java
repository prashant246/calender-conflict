package com.example.calender.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Event {
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String creator;
    private List<String> members;

}
