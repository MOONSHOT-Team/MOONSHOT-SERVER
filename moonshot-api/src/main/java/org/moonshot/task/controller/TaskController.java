package org.moonshot.task.controller;

import static org.moonshot.response.SuccessType.POST_TASK_SUCCESS;

import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.moonshot.jwt.JwtTokenProvider;
import org.moonshot.model.Logging;
import org.moonshot.response.MoonshotResponse;
import org.moonshot.task.dto.request.TaskSingleCreateRequestDto;
import org.moonshot.task.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/task")
public class TaskController implements TaskApi {

    private final TaskService taskService;

    @PostMapping
    @Logging(item = "Task", action = "Post")
    public ResponseEntity<MoonshotResponse<?>> createTask(final Principal principal, @RequestBody @Valid final TaskSingleCreateRequestDto request) {
        taskService.createTask(request, JwtTokenProvider.getUserIdFromPrincipal(principal));
        return ResponseEntity.status(HttpStatus.CREATED).body(MoonshotResponse.success(POST_TASK_SUCCESS));
    }

    @DeleteMapping("/{taskId}")
    @Logging(item = "Task", action = "Delete")
    public ResponseEntity<?> deleteTask (final Principal principal, @PathVariable("taskId") final Long taskId) {
        taskService.deleteTask(JwtTokenProvider.getUserIdFromPrincipal(principal), taskId);
        return ResponseEntity.noContent().build();
    }

}
