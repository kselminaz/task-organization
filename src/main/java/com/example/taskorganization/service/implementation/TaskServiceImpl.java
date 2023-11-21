package com.example.taskorganization.service.implementation;

import com.example.taskorganization.annotation.Log;
import com.example.taskorganization.dao.entity.TaskEntity;
import com.example.taskorganization.dao.repository.CategoryRepository;
import com.example.taskorganization.dao.repository.ProjectRepository;
import com.example.taskorganization.dao.repository.TaskRepository;
import com.example.taskorganization.exception.NotFoundException;
import com.example.taskorganization.model.criteria.PageCriteria;
import com.example.taskorganization.model.criteria.SortingCriteria;
import com.example.taskorganization.model.enums.TaskStatus;
import com.example.taskorganization.model.request.TaskSaveRequest;
import com.example.taskorganization.model.request.TaskUpdateRequest;
import com.example.taskorganization.model.response.PageableResponse;
import com.example.taskorganization.model.response.TaskResponse;
import com.example.taskorganization.service.abstraction.TaskService;
import com.example.taskorganization.util.SortingUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.example.taskorganization.mapper.TaskMapper.*;
import static com.example.taskorganization.model.enums.TaskStatus.DELETED;
import static lombok.AccessLevel.PRIVATE;

@Service
@Log
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TaskServiceImpl implements TaskService {

    TaskRepository taskRepository;

    ProjectRepository projectRepository;

    CategoryRepository categoryRepository;
    SortingUtil sortingUtil;

    UserDetailsServiceImpl userDetailsService;

    private TaskEntity fetchTaskIfExist(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(
                "Task with id [%d] was not found!", id
        )));
    }
    private Long getSignedUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userDetailsService.signedUser(authentication.getName()).getId();
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        var task = fetchTaskIfExist(id);
        return buildTaskResponse(task);
    }

    @Override
    public void deleteTask(Long id) {

        var task = fetchTaskIfExist(id);
        task.setStatus(DELETED);
        taskRepository.save(task);
    }

    @Override
    public PageableResponse<TaskResponse> getTasks(SortingCriteria sortingCriteria, PageCriteria pageCriteria) {

        var orders = sortingUtil.buildSortOrders(sortingCriteria);
        var tasksPage = taskRepository.findAllByCreatedByUserIs(PageRequest.of(pageCriteria.getPage(), pageCriteria.getCount(), Sort.by(orders)),getSignedUserId());
        return buildPageableResponse(tasksPage);
    }

    @Override
    public void saveTask(TaskSaveRequest request) {


        Long userId = getSignedUserId();

        var taskEntity = buildTaskEntity(userId, request);

        if (request.getProjectId() != null) {
            taskEntity.setProject(projectRepository.getReferenceById(request.getProjectId()));
        }
        if (request.getCategoryId() != null) {
            taskEntity.setCategory(categoryRepository.getReferenceById(request.getCategoryId()));
        }
        taskRepository.save(taskEntity);

    }

    @Override
    public void updateTask(Long id, TaskUpdateRequest request) {

        var taskEntity = fetchTaskIfExist(id);

        updateTaskEntity(taskEntity, request);

        if (request.getProjectId() != null) {
            taskEntity.setProject(projectRepository.getReferenceById(request.getProjectId()));
        }
        if (request.getCategoryId() != null) {
            taskEntity.setCategory(categoryRepository.getReferenceById(request.getCategoryId()));
        }

        taskRepository.save(taskEntity);
    }

    @Override
    public void updateTaskWithStatus(Long id, TaskStatus status) {

        var task = fetchTaskIfExist(id);

        task.setStatus(status);

        taskRepository.save(task);
    }


}
