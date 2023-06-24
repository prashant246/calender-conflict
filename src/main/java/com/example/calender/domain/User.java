package com.example.calender.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private String name;
    private List<Event> events;
    private List<Shifts> shifts;
    private List<Slots> busySlots;

    public User(String name) {
        this.name = name;
        this.events = new ArrayList<>();
        this.shifts = new ArrayList<>();
        this.busySlots = new ArrayList<>();
    }
}
