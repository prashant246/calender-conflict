package com.example.calender.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Slots {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
