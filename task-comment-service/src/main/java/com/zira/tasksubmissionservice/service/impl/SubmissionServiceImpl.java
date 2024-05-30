package com.zira.tasksubmissionservice.service.impl;

import com.zira.tasksubmissionservice.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.zira.tasksubmissionservice.entity.Submission;
import com.zira.tasksubmissionservice.repository.SubmissionRepository;
import com.zira.tasksubmissionservice.service.SubmissionService;
import com.zira.tasksubmissionservice.service.TaskService;
import com.zira.tasksubmissionservice.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Override
    public Submission submitTask(Long taskId, String githubLink, Long userId, String jwt) throws Exception {
        TaskDto task = taskService.getTaskById(taskId);
        if(task== null)
            throw new Exception("Task not found for this id "+taskId);

        Submission submission = new Submission();
        submission.setTaskId(taskId);
        submission.setUserId(userId);
        submission.setGithubLink(githubLink);
        submission.setSubmissionTime(LocalDateTime.now());
        return submissionRepository.save(submission);
    }

    @Override
    public Submission getTaskSubmissionById(Long submissionId) throws Exception {
        return submissionRepository.findById(submissionId)
                .orElseThrow(() -> new Exception("Task submission not found wit id"+ submissionId));
    }

    @Override
    public List<Submission> getAllTaskSubmissions() {
        return submissionRepository.findAll();
    }

    @Override
    public List<Submission> getTaskSubmissionsByTaskId(Long taskId) {
        return submissionRepository.findByTaskId(taskId);
    }

    @Override
    public Submission acceptOrDeclineSubmission(Long id, String status) throws Exception {
        Submission submission = getTaskSubmissionById(id);
        submission.setStatus(status);
        if (status.equals("ACCEPT"))
            taskService.completeTask(submission.getId());

        return submissionRepository.save(submission);

    }
}
