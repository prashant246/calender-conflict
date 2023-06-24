package com.example.calender.service.impl;

import com.example.calender.datasource.Database;
import com.example.calender.domain.Event;
import com.example.calender.domain.User;
import com.example.calender.mapper.MapperUtil;
import com.example.calender.model.requests.CreateEvent;
import com.example.calender.model.requests.GetEvent;
import com.example.calender.model.requests.UpdateEvent;
import com.example.calender.service.EventService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventServiceImpl implements EventService {
    @Override
    public void createEvent(CreateEvent createEvent) {
        User userByName = Database.getUserByName(createEvent.getCreator());
        List<User> usersByName = Database.getUsersByName(createEvent.getUsers());
        usersByName.add(userByName);
        Event event = MapperUtil.toEvent(createEvent);
        Database.addEvent(event);

        Database.addEventToUser(event, usersByName);
    }

    @Override
    public List<Event> getEvent(GetEvent getEvent) {
        List<Event> events = new ArrayList<>();
        if (getEvent.getName() != null) {
            Event eventByName = Database.getEventByName(getEvent.getName());
            if (eventByName != null) {
                events.add(eventByName);
            }
        }
        if (getEvent.getUser() != null) {
            List<Event> eventList = Database.getUserByName(getEvent.getUser()).getEvents();
            if (!CollectionUtils.isEmpty(eventList)) {
                events.addAll(eventList);
            }
        }

        return events;
    }

    @Override
    public void deleteEvent(UpdateEvent updateEvent) {
        Event eventByName = Database.getEventByName(updateEvent.getName());
        if (eventByName == null) {
            System.out.println("No event to delete");
        }
        if (eventByName.getCreator().equals(updateEvent.getUser())) {
            Database.deleteEvent(eventByName);
        } else {
            System.out.println("Not authorized to delete");
        }

    }
}
