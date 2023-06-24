package com.example.calender.model.requests;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateUser {
    private String name;
    private Shift shift;
    private Slot slot;

    @Data
    public static class Shift {
        LocalDateTime startTime;
        LocalDateTime endTime;
    }

    @Data
    public static class Slot {
        LocalDateTime startTime;
        LocalDateTime endTime;
    }
}
