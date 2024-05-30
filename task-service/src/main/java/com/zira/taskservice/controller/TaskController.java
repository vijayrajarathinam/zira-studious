package com.zira.taskservice.controller;


import com.zira.taskservice.UserDto;
import com.zira.taskservice.entity.Task;
import com.zira.taskservice.entity.TaskStatus;
import com.zira.taskservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zira.taskservice.service.TaskService;

import java.util.List;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestHeader("Authorization") String jwt) throws Exception {
        UserDto user = userService.getUserProfile(jwt);
        Task createdTask = taskService.createTask(task, user);

        return new ResponseEntity<Task>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
//            @RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long taskId
    ) throws Exception {
//        UserDto user = userService.getUserProfile(jwt);
        Task task = taskService.getTaskById(taskId);
        return new ResponseEntity<Task>(task, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Task>> getAssignedUserTask(
                        @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) TaskStatus taskstatus
    ) {
        UserDto user = userService.getUserProfile(jwt);
        List<Task> allUserTasks =  taskService.userAssignedTasks(user.getId(), taskstatus);
        return new ResponseEntity<List<Task>>(allUserTasks, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTask(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) TaskStatus taskstatus
    ) {
        List<Task> allUserTasks =  taskService.getAllTask(taskstatus);
        return new ResponseEntity<List<Task>>(allUserTasks, HttpStatus.OK);
    }

    @PutMapping("/assign/{id}/user/{userId}")
    public ResponseEntity<Task> assignTaskToUser(
            @PathVariable Long id,
            @PathVariable Long userId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Task allUserTasks =  taskService.assignToUser(userId, id);
        return new ResponseEntity<Task>(allUserTasks, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody Task task,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto user = userService.getUserProfile(jwt);
        Task updatedTask =  taskService.updateTask(id,task,user.getId());
        return new ResponseEntity<Task>(updatedTask, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}/completed")
    public ResponseEntity<Task> completeTask(@PathVariable Long id) throws Exception {
        Task updatedTask =  taskService.completeTask(id);
        return new ResponseEntity<Task>(updatedTask, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) throws Exception {
        taskService.deleteTask(id);
        return new ResponseEntity<String >("Deleted successfully", HttpStatus.OK);
    }




}
