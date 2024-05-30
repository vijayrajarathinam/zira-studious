package com.zira.taskservice.service.impl;

import com.zira.taskservice.UserDto;
import com.zira.taskservice.entity.Task;
import com.zira.taskservice.entity.TaskStatus;
import com.zira.taskservice.repository.TaskRepository;
import com.zira.taskservice.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;


    @Override
    public Task createTask(Task task, UserDto user) throws Exception {
        if(!user.getRole().equals("ROLE_ADMIN"))
            throw new Exception("Only Admin can create tasks");
        task.setAssignedUserId(user.getId());
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());
        taskRepository.save(task);
        return task;
    }

    @Override
    public Task getTaskById(Long id) throws Exception {
        return taskRepository.findById(id).orElseThrow(()-> new Exception("Task not found"));
    }

    @Override
    public List<Task> getAllTask(TaskStatus status) {
//        List<Task> allTask = taskRepository.findAll();
//        List<Task> statusTask  = allTask.stream()
//                .filter(task -> task.getStatus().name().equalsIgnoreCase(status.toString()))
//                .collect(Collectors.toList());

        return taskRepository.findByStatus(status);
    }

    @Override
    public Task updateTask(Long id, Task updatedTask, Long userId) throws Exception {
        Task currTask = getTaskById(id);

        if(updatedTask.getTitle() != null) currTask.setTitle(updatedTask.getTitle());
        if(updatedTask.getDescription() != null) currTask.setDescription(updatedTask.getDescription());
        if(updatedTask.getImage() != null) currTask.setImage(updatedTask.getImage());
        if(updatedTask.getStatus() != null) currTask.setStatus(updatedTask.getStatus());
        if(updatedTask.getDeadLine() != null) currTask.setDeadLine(updatedTask.getDeadLine());
        return taskRepository.save(currTask);
    }

    @Override
    public void deleteTask(Long id) throws Exception {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    @Override
    public Task assignToUser(Long userId, Long taskId) throws Exception {
        Task existingTask = getTaskById(taskId);
        existingTask.setAssignedUserId(userId);
        existingTask.setStatus(TaskStatus.DONE);
        return taskRepository.save(existingTask);
    }

    @Override
    public List<Task> userAssignedTasks(Long userId, TaskStatus status) {
        List<Task> allUserTask = taskRepository.findByAssignedUserId(userId);


        return allUserTask.stream()
                .filter(task -> task.getStatus().name().equals(status.name()))
                .collect(Collectors.toList());
    }

    @Override
    public Task completeTask(Long taskId) throws Exception {
        Task task = getTaskById(taskId);
        task.setStatus(TaskStatus.DONE);
        return taskRepository.save(task);
    }
}
