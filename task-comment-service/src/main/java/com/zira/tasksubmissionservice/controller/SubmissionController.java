package com.zira.tasksubmissionservice.controller;


import com.zira.tasksubmissionservice.dto.UserDto;
import com.zira.tasksubmissionservice.entity.Submission;
import com.zira.tasksubmissionservice.service.SubmissionService;
import com.zira.tasksubmissionservice.service.TaskService;
import com.zira.tasksubmissionservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submission")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<Submission> submitTask(
            @RequestParam Long task_id,
            @RequestParam String github_link,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto user = userService.getUserProfile(jwt);
        Submission submission = submissionService.submitTask(task_id,github_link, user.getId(), jwt);

        return new ResponseEntity<Submission>(submission, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(
            @PathVariable Long id
    ) throws Exception {
        Submission submission = submissionService.getTaskSubmissionById(id);
        return new ResponseEntity<Submission>(submission, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Submission>> getAllSubmission(){
        return new ResponseEntity<List<Submission>>(
                submissionService.getAllTaskSubmissions(),
                HttpStatus.OK
        );
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Submission>> getAllSubmissionForTask(
            @RequestParam Long taskId
    ){
        return new ResponseEntity<List<Submission>>(
                submissionService.getTaskSubmissionsByTaskId(taskId),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Submission> acceptOrDeclineSubmission(
            @PathVariable Long id,
            @RequestParam String status
    ) throws Exception {
        return  new ResponseEntity<Submission>(
                submissionService.acceptOrDeclineSubmission(id, status),
                HttpStatus.ACCEPTED
        );
    }
}
