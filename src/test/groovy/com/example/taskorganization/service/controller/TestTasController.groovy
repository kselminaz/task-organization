package com.example.taskorganization.service.controller

import com.example.taskorganization.controller.TaskController
import com.example.taskorganization.exception.ErrorHandler
import com.example.taskorganization.model.response.TaskResponse
import com.example.taskorganization.service.abstraction.TaskService
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

class TestTasController extends Specification{
    private EnhancedRandom random= EnhancedRandomBuilder.aNewEnhancedRandom();
    private TaskService taskService;
    private MockMvc mockMvc;

    void setup(){

        taskService= Mock();
        def taskController=new TaskController(taskService);
        mockMvc= MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new ErrorHandler())
                .build()
    }

    def "Test GetTaskById"(){
        given:
        def id=1L
        def url="/v1/tasks/$id"

        def responseView=random.nextObject(TaskResponse)
        def expectedResponse="""
                                    {
                                   "id": $responseView.id,                         
                                   "name":  $responseView.name,
                                   "description":  $responseView.description,
                                   "status":  $responseView.status,
                                   "priority":  $responseView.priority,
                                   "deadline":  $responseView.deadline,
                                   "category":  $responseView.category,
                                   "project":  $responseView.project,
                                   "createdAt":  $responseView.createdAt,

                                    }
                                 """
        when:
        def result=mockMvc.perform(
                get(url)
        ).andReturn()

        then:
        1 * taskService.getTaskById(id) >> responseView
        def response=result.response
        response.status== HttpStatus.OK.value()
        JSONAssert.assertEquals(expectedResponse,response.getContentAsString(),false)

    }

}
