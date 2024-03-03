package org.moonshot.task.controller;

import static org.moonshot.response.SuccessType.POST_TASK_SUCCESS;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.moonshot.model.Logging;
import org.moonshot.response.MoonshotResponse;
import org.moonshot.task.dto.request.TaskSingleCreateRequestDto;
import org.moonshot.task.service.TaskService;
import org.moonshot.user.model.LoginUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/task")
public class TaskController implements TaskApi {

    private final TaskService taskService;

    @PostMapping
    @Logging(item = "Task", action = "Post")
    public ResponseEntity<MoonshotResponse<?>> createTask(@LoginUser Long userId, @RequestBody @Valid final TaskSingleCreateRequestDto request) {
        taskService.createTask(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(MoonshotResponse.success(POST_TASK_SUCCESS));
    }

    @DeleteMapping("/{taskId}")
    @Logging(item = "Task", action = "Delete")
    public ResponseEntity<?> deleteTask (@LoginUser Long userId, @PathVariable("taskId") final Long taskId) {
        taskService.deleteTask(userId, taskId);
        return ResponseEntity.noContent().build();
    }

}
