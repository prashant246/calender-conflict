package com.example.calender.service;

import com.example.calender.domain.User;
import com.example.calender.model.requests.CreateUser;
import com.example.calender.model.requests.GetUser;
import com.example.calender.model.requests.UpdateUser;

public interface UserService {
    public void createUser(CreateUser createUser);

    public void updateUser(UpdateUser updateUser) throws Exception;

    User getUser(GetUser getUser) throws Exception;
}
