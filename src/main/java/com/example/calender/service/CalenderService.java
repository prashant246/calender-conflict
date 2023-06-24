package com.example.calender.service;

import com.example.calender.domain.Event;
import com.example.calender.model.requests.FindSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CalenderService {

    public LocalDateTime findFavorableSlot(FindSlot findSlot);

    public List<Event> fetchConflictingEvents(String userName, LocalDate date);
}
