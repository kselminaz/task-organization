package com.example.taskorganization.mapper;

import com.example.taskorganization.dao.entity.ProjectEntity;
import com.example.taskorganization.dao.entity.TaskEntity;
import com.example.taskorganization.model.request.TaskSaveRequest;
import com.example.taskorganization.model.request.TaskUpdateRequest;
import com.example.taskorganization.model.response.PageableResponse;
import com.example.taskorganization.model.response.TaskResponse;
import org.springframework.data.domain.Page;

import static com.example.taskorganization.model.enums.TaskStatus.CREATED;
import static java.util.Optional.ofNullable;


public class TaskMapper {

    public static TaskResponse buildTaskResponse(TaskEntity entity) {
        return TaskResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .priority(entity.getPriority())
                .deadline(entity.getDeadline())
                .project(ofNullable(entity.getProject()).isPresent() ? entity.getProject().getName() : null)
                .category(ofNullable(entity.getCategory()).isPresent() ? entity.getCategory().getName() : null)
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static TaskEntity buildTaskEntity(Long userId, TaskSaveRequest request) {
        return TaskEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdByUser(userId)
                .status(CREATED)
                .priority(request.getPriority())
                .deadline(request.getDeadline())
                .build();
    }

    public static void updateTaskEntity(TaskEntity entity, TaskUpdateRequest request) {

        ofNullable(request.getName()).ifPresent(entity::setName);
        ofNullable(request.getDescription()).ifPresent(entity::setDescription);
        ofNullable(request.getStatus()).ifPresent(entity::setStatus);
        ofNullable(request.getPriority()).ifPresent(entity::setPriority);
        ofNullable(request.getDeadline()).ifPresent(entity::setDeadline);

    }

    public static PageableResponse<TaskResponse> buildPageableResponse(Page<TaskEntity> tasksPage) {

        var list = tasksPage.getContent().stream().map(TaskMapper::buildTaskResponse).toList();
        return PageableResponse.<TaskResponse>builder()
                .data(list)
                .hasNextPage(tasksPage.hasNext())
                .lastPageNumber(tasksPage.getTotalPages())
                .totalElements(tasksPage.getTotalElements())
                .build();

    }
}
