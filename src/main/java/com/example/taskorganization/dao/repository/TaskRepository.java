package com.example.taskorganization.dao.repository;

import com.example.taskorganization.dao.entity.TaskEntity;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @EntityGraph(value = "taskWithProjectAndCategory")
    Page<TaskEntity> findAllByCreatedByUserIs(Pageable pageable,Long userId);
}
