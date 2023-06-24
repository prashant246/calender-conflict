package com.example.calender.mapper;

import com.example.calender.domain.Event;
import com.example.calender.domain.User;
import com.example.calender.model.requests.CreateEvent;
import com.example.calender.model.requests.CreateUser;

public class MapperUtil {

    public static User toUser(CreateUser createUser) {
        return new User(createUser.getName());
    }

    public static Event toEvent(CreateEvent createEvent) {
        return Event.builder()
                .members(createEvent.getUsers())
                .creator(createEvent.getCreator())
                .startTime(createEvent.getStartTime())
                .endTime(createEvent.getEndTime())
                .title(createEvent.getName())
                .build();
    }
}
