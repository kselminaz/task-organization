package com.example.taskorganization.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSaveRequest {

    @NotBlank(message = "Task name is required")
    private String name;

    private String description;

    private Integer priority;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime deadline;

    private Long categoryId;

    private Long projectId;

}
