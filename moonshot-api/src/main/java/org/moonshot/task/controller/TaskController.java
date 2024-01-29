package org.moonshot.task.controller;

import static org.moonshot.response.SuccessType.POST_TASK_SUCCESS;

import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.moonshot.jwt.JwtTokenProvider;
import org.moonshot.response.MoonshotResponse;
import org.moonshot.task.dto.request.TaskSingleCreateRequestDto;
import org.moonshot.task.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<MoonshotResponse<?>> createTask(final Principal principal, @RequestBody @Valid final TaskSingleCreateRequestDto request) {
        taskService.createTask(request, JwtTokenProvider.getUserIdFromPrincipal(principal));
        return ResponseEntity.status(HttpStatus.CREATED).body(MoonshotResponse.success(POST_TASK_SUCCESS));
    }

}
