package com.example.taskorganization.dao.repository;

import com.example.taskorganization.dao.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {
}
