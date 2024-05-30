package com.zira.userservice.service;

import com.zira.userservice.entity.User;

import java.util.List;


public interface UserService {
    public User getUserProfile(String jwt);
    public List<User> getAllUsers();
}
