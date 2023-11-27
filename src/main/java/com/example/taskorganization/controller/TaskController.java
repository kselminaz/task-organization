package com.example.taskorganization.controller;

import com.example.taskorganization.config.security.SignedUserDetails;
import com.example.taskorganization.dao.entity.UserEntity;
import com.example.taskorganization.model.criteria.PageCriteria;
import com.example.taskorganization.model.criteria.SortingCriteria;
import com.example.taskorganization.model.enums.TaskStatus;
import com.example.taskorganization.model.request.TaskSaveRequest;
import com.example.taskorganization.model.request.TaskUpdateRequest;
import com.example.taskorganization.model.response.PageableResponse;
import com.example.taskorganization.model.response.TaskResponse;
import com.example.taskorganization.service.abstraction.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping
    public PageableResponse<TaskResponse> getTasks(SortingCriteria sortingCriteria, PageCriteria pageCriteria) {
        return taskService.getTasks(sortingCriteria, pageCriteria);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void saveTask(@Valid @RequestBody TaskSaveRequest request) {

        taskService.saveTask(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateTask(@PathVariable Long id, @RequestBody TaskUpdateRequest request) {
        taskService.updateTask(id, request);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateTaskWithStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        taskService.updateTaskWithStatus(id, status);
    }

}
