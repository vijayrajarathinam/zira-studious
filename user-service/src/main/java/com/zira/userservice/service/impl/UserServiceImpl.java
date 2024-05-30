package com.zira.userservice.service.impl;

import com.zira.userservice.config.JwtTokenProvider;
import com.zira.userservice.entity.User;
import com.zira.userservice.repository.UserRepository;
import com.zira.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserProfile(String jwt) {
        String email = JwtTokenProvider.getEmailFromJwtToken(jwt);
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
