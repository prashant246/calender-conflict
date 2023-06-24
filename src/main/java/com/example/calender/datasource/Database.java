package com.example.calender.datasource;

import com.example.calender.domain.Event;
import com.example.calender.domain.Shifts;
import com.example.calender.domain.Slots;
import com.example.calender.domain.User;
import com.example.calender.model.requests.UpdateUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Database {

    public static List<User> users;
    public static List<Event> events;

    public static void addUser(User user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public static User getUserByName(String name) {
        Optional<User> first = users.stream().filter(user -> user.getName().equals(name)).findFirst();
        if (!first.isPresent()) {
            return null;
        }
        return first.get();
    }

    public static void updateUser(User user, UpdateUser updateUser) {
        if (updateUser.getShift() != null) {
            user.getShifts().add(Shifts.builder()
                    .startTime(updateUser.getShift().getStartTime())
                    .endTime(updateUser.getShift().getEndTime())
                    .build());
        }

        if (updateUser.getSlot() != null) {
            user.getBusySlots().add(Slots.builder()
                    .startTime(updateUser.getSlot().getStartTime())
                    .endTime(updateUser.getSlot().getEndTime())
                    .build());
        }
    }

    public static List<User> getUsersByName(List<String> members) {
        List<User> usersList = new ArrayList<>();
        for (User user : users) {
            if (members.contains(user.getName())) {
                usersList.add(user);
            }
        }
        return usersList;
    }

    public static void addEvent(Event createEvent) {
        if (events == null) {
            events = new ArrayList<>();
        }
        events.add(createEvent);
    }

    public static void addEventToUser(Event event, List<User> usersByName) {
        usersByName.stream().forEach(user ->
        {
            user.getEvents().add(event);
            user.getBusySlots().add(Slots.builder().startTime(event.getStartTime())
                    .endTime(event.getEndTime())
                    .build());
        });
    }

    public static Event getEventByName(String name) {
        Optional<Event> first = events.stream().filter(event -> name.equals(event.getTitle())).findFirst();

        if (!first.isPresent()) {
            return null;
        }
        return first.get();
    }

    public static void deleteEvent(Event event) {
        events.remove(event);
        List<String> members = event.getMembers();
        members.add(event.getCreator());
        List<User> usersByName = getUsersByName(members);
        usersByName.stream().forEach(user -> user.getEvents().remove(event));
    }
}
