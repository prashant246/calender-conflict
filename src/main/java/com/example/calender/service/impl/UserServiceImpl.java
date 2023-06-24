package com.example.calender.service.impl;

import com.example.calender.datasource.Database;
import com.example.calender.domain.User;
import com.example.calender.mapper.MapperUtil;
import com.example.calender.model.requests.CreateUser;
import com.example.calender.model.requests.GetUser;
import com.example.calender.model.requests.UpdateUser;
import com.example.calender.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Override
    public void createUser(CreateUser createUser) {
        User user = MapperUtil.toUser(createUser);
        Database.addUser(user);
    }

    @Override
    public void updateUser(UpdateUser updateUser) throws Exception {
        User userByName = Database.getUserByName(updateUser.getName());
        if (userByName == null) {
            throw new Exception("No User Found");
        }

        Database.updateUser(userByName, updateUser);
    }

    @Override
    public User getUser(GetUser getUser) throws Exception {
        User userByName = Database.getUserByName(getUser.getName());
        if (userByName == null) {
            throw new Exception("No User Found");
        }
        return userByName;
    }
}
