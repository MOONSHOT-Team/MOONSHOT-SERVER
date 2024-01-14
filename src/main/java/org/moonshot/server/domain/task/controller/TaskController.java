package org.moonshot.server.domain.task.controller;

import static org.moonshot.server.global.common.response.SuccessType.*;

import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.task.dto.request.TaskSingleCreateRequestDto;
import org.moonshot.server.domain.task.service.TaskService;
import org.moonshot.server.global.auth.jwt.JwtTokenProvider;
import org.moonshot.server.global.common.response.MoonshotResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/task")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<MoonshotResponse<?>> createTask(Principal principal, @RequestBody @Valid TaskSingleCreateRequestDto request) {
        taskService.createTask(request, JwtTokenProvider.getUserIdFromPrincipal(principal));
        return ResponseEntity.status(HttpStatus.CREATED).body(MoonshotResponse.success(POST_TASK_SUCCESS));
    }

}
