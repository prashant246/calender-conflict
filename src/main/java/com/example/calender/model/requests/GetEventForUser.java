package com.example.calender.model.requests;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GetEventForUser {

    private String name;
    private LocalDate date;
}
