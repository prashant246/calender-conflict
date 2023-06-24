package com.example.calender.controller;

import com.example.calender.domain.Event;
import com.example.calender.domain.User;
import com.example.calender.model.requests.*;
import com.example.calender.service.CalenderService;
import com.example.calender.service.EventService;
import com.example.calender.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;
    private final CalenderService calenderService;
    private final EventService eventService;

    @Autowired
    public UserController(UserService userService, CalenderService calenderService, EventService eventService) {
        this.userService = userService;
        this.calenderService = calenderService;
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody CreateUser createUser) {
        userService.createUser(createUser);
        return new ResponseEntity("Created", HttpStatus.CREATED);
    }

    @PostMapping(value = "/detail")
    public ResponseEntity getUser(@RequestBody GetUser getUser) {
        try {
            User user = userService.getUser(getUser);
            return new ResponseEntity(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Failed", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping(value = "/update")
    public ResponseEntity updateUser(@RequestBody UpdateUser updateUser) {
        try {
            userService.updateUser(updateUser);
            return new ResponseEntity("Updated", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/find-slot")
    public ResponseEntity findSlot(@RequestBody FindSlot findSlot) {
        try {
            LocalDateTime favorableSlot = calenderService.findFavorableSlot(findSlot);
            return new ResponseEntity(
                    String.format("Preferred Slot is %s - %s", favorableSlot, favorableSlot.plusHours(findSlot.getDuration()))
                    , HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity("Failed", HttpStatus.NOT_MODIFIED);
        }
    }

    @PostMapping(value = "/fetch-conflict")
    public ResponseEntity fetchConflictingEvents(@RequestBody GetEventForUser getEventForUser) {
        try {
            List<Event> events = calenderService.fetchConflictingEvents(getEventForUser.getName(),
                    getEventForUser.getDate());
            return new ResponseEntity(events, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity("Failed", HttpStatus.NOT_MODIFIED);
        }
    }

    @PostMapping(value = "/create-event")
    public ResponseEntity createEvent(@RequestBody CreateEvent createEvent) {
        try {
            eventService.createEvent(createEvent);
            return new ResponseEntity("Created", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity("Failed", HttpStatus.NOT_MODIFIED);
        }
    }

    @PostMapping(value = "/get-event")
    public ResponseEntity getEvents(@RequestBody GetEvent getEvent) {
        try {
            List<Event> events = eventService.getEvent(getEvent);
            return new ResponseEntity(events, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity("Failed", HttpStatus.NOT_MODIFIED);
        }
    }

    @PostMapping(value = "/delete-event")
    public ResponseEntity deleteEvent(@RequestBody UpdateEvent updateEvent) {
        try {
            eventService.deleteEvent(updateEvent);
            return new ResponseEntity("deleted", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity("Failed", HttpStatus.NOT_MODIFIED);
        }
    }
}
