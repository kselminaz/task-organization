package com.example.taskorganization.service

import com.example.taskorganization.dao.entity.CategoryEntity
import com.example.taskorganization.dao.entity.ProjectEntity
import com.example.taskorganization.dao.entity.TaskEntity
import com.example.taskorganization.dao.repository.CategoryRepository
import com.example.taskorganization.dao.repository.ProjectRepository
import com.example.taskorganization.dao.repository.TaskRepository
import com.example.taskorganization.exception.NotFoundException

import com.example.taskorganization.model.enums.TaskStatus
import com.example.taskorganization.model.request.TaskUpdateRequest
import com.example.taskorganization.service.abstraction.TaskService
import com.example.taskorganization.service.implementation.TaskServiceImpl
import com.example.taskorganization.service.implementation.UserDetailsServiceImpl
import com.example.taskorganization.util.SortingUtil
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import spock.lang.Specification

class TestTaskService extends Specification {

    EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom()
    TaskRepository taskRepository
    ProjectRepository projectRepository
    CategoryRepository categoryRepository
    SortingUtil sortingUtil
    UserDetailsServiceImpl userDetailsService
    TaskService taskService
    Authentication authentication
    void setup() {

        taskRepository = Mock()
        projectRepository = Mock()
        categoryRepository = Mock()
        userDetailsService = Mock()
        authentication=new UsernamePasswordAuthenticationToken("test", "", null);
        taskService = new TaskServiceImpl(taskRepository, projectRepository, categoryRepository, sortingUtil, userDetailsService)

    }

    def "Test getTaskById success"() {
        given:
        def id = enhancedRandom.nextLong()
        def entity = enhancedRandom.nextObject(TaskEntity)
        def response = TaskMapper.buildTaskResponse(entity)
        when:
        def actual = taskService.getTaskById(id)
        then:
        1 * taskRepository.findById(id) >> Optional.of(entity)
        response == actual

    }

    def "Test getTaskById data not found"() {
        given:
        def id = enhancedRandom.nextLong()
        when:
        taskService.getTaskById(id)
        then:
        1 * taskRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()

    }

    def "Test UpdateTaskWithStatus success"() {

        given:
        def id = enhancedRandom.nextLong()
        def status = TaskStatus.COMPLETED
        def task = enhancedRandom.nextObject(TaskEntity, "status")
        task.status = status
        when:
        taskService.updateTaskWithStatus(id, status)
        then:
        1 * taskRepository.findById(id) >> Optional.of(task)
        1 * taskRepository.save(task)

    }

    def "Test UpdateTaskWithStatus  not found task"() {

        given:
        def id = enhancedRandom.nextLong()
        when:
        taskService.getTaskById(id)
        then:
        1 * taskRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.message == String.format("Task with id [%d] was not found!", id)

    }

    def "Test updateTask success"() {
        given:
        def id = enhancedRandom.nextLong()
        def request = enhancedRandom.nextObject(TaskUpdateRequest)
        def taskEntity = enhancedRandom.nextObject(TaskEntity)

        def projectReference = enhancedRandom.nextObject(ProjectEntity)
        def categoryReference = enhancedRandom.nextObject(CategoryEntity)

        when:
        taskService.updateTask(id, request)
        then:

        1 * taskRepository.findById(id) >> Optional.of(taskEntity)
        1 * projectRepository.getReferenceById(request.projectId) >> projectReference
        1 * categoryRepository.getReferenceById(request.categoryId) >> categoryReference
        1 * taskRepository.save(taskEntity)

        request.name == taskEntity.name
        request.deadline == taskEntity.deadline
        request.description == taskEntity.description
        request.priority == taskEntity.priority
        request.status == taskEntity.status



    }

    def "Test updateTask error when task not found"() {
        given:
        def id = enhancedRandom.nextLong()
        def request = enhancedRandom.nextObject(TaskUpdateRequest)
        when:
        taskService.updateTask(id, request)
        then:
        1 * taskRepository.findById(id) >> Optional.empty()
        0 * taskRepository.save()
        NotFoundException ex = thrown()
        ex.message == String.format("Task with id [%d] was not found!", id)

    }


}
