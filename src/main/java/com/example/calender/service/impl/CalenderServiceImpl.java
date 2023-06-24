package com.example.calender.service.impl;

import com.example.calender.datasource.Database;
import com.example.calender.domain.Event;
import com.example.calender.domain.Shifts;
import com.example.calender.domain.User;
import com.example.calender.model.requests.FindSlot;
import com.example.calender.service.CalenderService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CalenderServiceImpl implements CalenderService {

    @Override
    public LocalDateTime findFavorableSlot(FindSlot findSlot) {
        findSlot.getUsers().add(findSlot.getCreator());
        List<User> users = Database.getUsersByName(findSlot.getUsers());
        Duration duration = Duration.ofHours(findSlot.getDuration());
        List<Event> allEvents = new ArrayList<>();
        List<Shifts> shifts = new ArrayList<>();

        // Retrieve shift timings and calendars of all users
        for (User user : users) {
            allEvents.addAll(user.getEvents());
            shifts.addAll(user.getShifts());
        }

        // Determine the common shift range
        LocalDateTime shiftStart = shifts.stream().map(Shifts::getStartTime).max(LocalDateTime::compareTo).orElse(null);
        LocalDateTime shiftEnd = shifts.stream().map(Shifts::getEndTime).min(LocalDateTime::compareTo).orElse(null);

        // If no events are present within the shift range, return the start time of the shift range
        if (allEvents.stream().noneMatch(event -> event.getStartTime().isBefore(shiftEnd) && event.getEndTime().isAfter(shiftStart))) {
            return shiftStart;
        }

        // Filter events within the common shift range
        List<Event> eventsWithinShift = allEvents.stream()
                .filter(event -> (event.getStartTime().isAfter(shiftStart) || event.getStartTime().isEqual(shiftStart))
                        && (event.getEndTime().isBefore(shiftEnd) || event.getEndTime().isEqual(shiftEnd)))
                .collect(Collectors.toList());

        // Sort events within the shift range in ascending order based on start time
        Collections.sort(eventsWithinShift, Comparator.comparing(Event::getStartTime));

        LocalDateTime latestEndTime = shiftStart;

        // Check if the first event starts after the shift start time
        if (!eventsWithinShift.isEmpty()) {
            Event firstEvent = eventsWithinShift.get(0);
            LocalDateTime firstEventStartTime = firstEvent.getStartTime();

            // Check if there is a gap from the shift start time to the start of the first event
            if (firstEventStartTime.isAfter(shiftStart)) {
                Duration gapDuration = Duration.between(shiftStart, firstEventStartTime);

                // Check if the gap duration is suitable for the desired duration
                if (gapDuration.compareTo(duration) >= 0) {
                    return shiftStart; // Return the start time of the gap
                }
            }
        }

        // Iterate through the events within the shift range to find the most favorable slot
        for (Event event : eventsWithinShift) {
            LocalDateTime eventStartTime = event.getStartTime();
            LocalDateTime eventEndTime = event.getEndTime();

            // Check if there is a gap between the current event and the latest end time
            if (eventStartTime.isAfter(latestEndTime)) {
                Duration gapDuration = Duration.between(latestEndTime, eventStartTime);

                // Check if the gap duration is suitable for the desired duration
                if (gapDuration.compareTo(duration) >= 0) {
                    return latestEndTime; // Return the start time of the gap
                }
            }

            // Update the latest end time if the current event's end time is later
            if (eventEndTime.isAfter(latestEndTime)) {
                latestEndTime = eventEndTime;
            }
        }

        // Check if there is a gap between the latest event end time and the shift end time
        if (latestEndTime.isBefore(shiftEnd)) {
            Duration gapDuration = Duration.between(latestEndTime, shiftEnd);

            // Check if the gap duration is suitable for the desired duration
            if (gapDuration.compareTo(duration) >= 0) {
                return latestEndTime; // Return the start time of the gap
            }
        }

        return null;
    }

    @Override
    public List<Event> fetchConflictingEvents(String userName, LocalDate date) {
        User user = Database.getUserByName(userName);
        List<Event> conflictingEvents = new ArrayList<>();

        // Iterate over the user's events on the specified date
        for (Event event : user.getEvents()) {
            LocalDateTime eventStartTime = event.getStartTime();
            LocalDateTime eventEndTime = event.getEndTime();

            // Check if the event overlaps with the specified date
            if (eventStartTime.toLocalDate().equals(date)) {
                // Iterate over the user's other events to check for conflicts
                for (Event otherEvent : user.getEvents()) {
                    if (!otherEvent.equals(event)) {
                        LocalDateTime otherEventStartTime = otherEvent.getStartTime();
                        LocalDateTime otherEventEndTime = otherEvent.getEndTime();

                        // Check if there is an overlap between events
                        if (eventStartTime.isBefore(otherEventEndTime) && eventEndTime.isAfter(otherEventStartTime)) {
                            conflictingEvents.add(event);
                            break;  // Break the inner loop once a conflict is found
                        }
                    }
                }
            }
        }

        return conflictingEvents;
    }

}
