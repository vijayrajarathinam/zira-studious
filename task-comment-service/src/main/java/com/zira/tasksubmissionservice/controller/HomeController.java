package com.zira.tasksubmissionservice.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

    public ResponseEntity<String> homePage(){
        return new ResponseEntity<String>("Hello world", HttpStatus.OK);
    }
}
