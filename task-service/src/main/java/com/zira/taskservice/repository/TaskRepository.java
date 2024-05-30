package com.zira.taskservice.repository;

import com.zira.taskservice.entity.Task;
import com.zira.taskservice.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus taskStatus);

    List<Task> findByAssignedUserId(Long userId);
}
