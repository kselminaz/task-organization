package com.example.taskorganization.model.request;

import com.example.taskorganization.model.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateRequest {

    private String name;

    private String description;

    private Integer priority;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm a")

    private LocalDateTime deadline;

    private TaskStatus status;

    private Long categoryId;

    private Long projectId;

}
