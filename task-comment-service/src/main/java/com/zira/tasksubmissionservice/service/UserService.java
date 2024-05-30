package com.zira.tasksubmissionservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.zira.tasksubmissionservice.dto.UserDto;


@FeignClient(name = "USER-SERVICE", url = "http://localhost:5001")
public interface UserService {

    @GetMapping("/api/user/profile")
    public UserDto getUserProfile(@RequestHeader("Authorization") String jwt);

}
