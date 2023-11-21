package com.example.taskorganization.dao.repository;

import com.example.taskorganization.dao.entity.ChangePasswordEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ChangePasswordRepository extends CrudRepository<ChangePasswordEntity,Long> {

    Optional<ChangePasswordEntity> findByCodeAndExpirationTimeAfter(String code, LocalDateTime now);
}
