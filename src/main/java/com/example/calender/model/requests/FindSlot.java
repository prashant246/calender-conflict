package com.example.calender.model.requests;

import lombok.Data;

import java.util.List;

@Data
public class FindSlot {

    private String creator;
    private List<String> users;
    private int duration;
}
