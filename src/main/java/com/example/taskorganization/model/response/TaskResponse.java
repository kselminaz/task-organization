package com.example.taskorganization.model.response;

import com.example.taskorganization.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponse {

    private Long id;

    private String name;

    private String description;

    private TaskStatus status;

    private Integer priority;

    private LocalDateTime deadline;

    private String category;

    private String project;

    private LocalDateTime createdAt;
}
