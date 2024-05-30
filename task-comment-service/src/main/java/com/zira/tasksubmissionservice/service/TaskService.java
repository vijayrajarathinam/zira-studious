package com.zira.tasksubmissionservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.zira.tasksubmissionservice.dto.TaskDto;
import org.springframework.web.bind.annotation.PutMapping;


@FeignClient(name = "TASK-SERVICE", url = "http://localhost:5002")
public interface TaskService {

    @GetMapping("/api/tasks/{id}")
    public TaskDto getTaskById(@PathVariable Long id) throws Exception;

    @PutMapping("/api/tasks/{id}/completed")
    public TaskDto completeTask(@PathVariable Long id) throws Exception;
}

