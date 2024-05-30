package com.zira.taskservice.controller;


import com.zira.taskservice.UserDto;
import com.zira.taskservice.entity.Task;
import com.zira.taskservice.entity.TaskStatus;
import com.zira.taskservice.service.TaskService;
import com.zira.taskservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class DashBoardController {

   @Autowired
   private UserService userService;

   @Autowired
   private TaskService taskService;

   @GetMapping("/tasks")
   public ResponseEntity<List<Task>> getUserAssignedTasks(
           @RequestHeader("Authorization") String jwt,
           @RequestParam(name = "status", required = false) TaskStatus status
   ){
       UserDto user = userService.getUserProfile(jwt);
       return new ResponseEntity<List<Task>>(
               taskService.userAssignedTasks(
                       user.getId(),
                       status == null? TaskStatus.PENDING: status
               ),
               HttpStatus.OK
       );
   }
}
