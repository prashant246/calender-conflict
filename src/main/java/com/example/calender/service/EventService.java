package com.example.calender.service;

import com.example.calender.domain.Event;
import com.example.calender.model.requests.CreateEvent;
import com.example.calender.model.requests.GetEvent;
import com.example.calender.model.requests.UpdateEvent;

import java.util.List;

public interface EventService {

    public void createEvent(CreateEvent createEvent);

    public List<Event> getEvent(GetEvent getEvent);

    public void deleteEvent(UpdateEvent updateEvent);
}
