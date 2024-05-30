package com.zira.taskservice.service;

import com.zira.taskservice.UserDto;
import com.zira.taskservice.entity.Task;
import com.zira.taskservice.entity.TaskStatus;

import java.util.List;

public interface TaskService {
    Task createTask(Task task, UserDto requesterRole) throws Exception;
    Task getTaskById(Long id) throws Exception;
    List<Task> getAllTask(TaskStatus status);
    Task updateTask(Long id, Task updatedTask, Long userId) throws Exception;
    void deleteTask(Long id) throws Exception;
    Task assignToUser(Long userId, Long taskId) throws Exception;
    List<Task> userAssignedTasks(Long userId, TaskStatus status);
    Task completeTask(Long taskId) throws Exception;

}
