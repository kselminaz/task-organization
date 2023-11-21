package com.example.taskorganization.service.abstraction;

import com.example.taskorganization.model.criteria.PageCriteria;
import com.example.taskorganization.model.criteria.SortingCriteria;
import com.example.taskorganization.model.enums.TaskStatus;
import com.example.taskorganization.model.request.TaskSaveRequest;
import com.example.taskorganization.model.request.TaskUpdateRequest;
import com.example.taskorganization.model.response.PageableResponse;
import com.example.taskorganization.model.response.TaskResponse;
import org.springframework.security.core.Authentication;

public interface TaskService {

    TaskResponse getTaskById(Long id);

    void updateTask(Long id, TaskUpdateRequest request);

    void updateTaskWithStatus(Long id, TaskStatus status);

    void deleteTask(Long id);

    PageableResponse<TaskResponse> getTasks(SortingCriteria sortingCriteria, PageCriteria pageCriteria);

    void saveTask(TaskSaveRequest request);
}




