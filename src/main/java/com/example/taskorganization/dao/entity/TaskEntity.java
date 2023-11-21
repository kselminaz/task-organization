package com.example.taskorganization.dao.entity;

import com.example.taskorganization.model.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
@Builder
@NamedEntityGraph(
        name = "taskWithProjectAndCategory",
        attributeNodes = {
                @NamedAttributeNode("category"),
                @NamedAttributeNode("project")
        }

)
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Long createdByUser;

    @Enumerated(STRING)
    private TaskStatus status;

    private Integer priority;

    @Temporal(TIMESTAMP)
    private LocalDateTime deadline;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private CategoryEntity category;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "project_id")
    @ToString.Exclude
    private ProjectEntity project;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
