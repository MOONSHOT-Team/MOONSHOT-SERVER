package org.moonshot.server.domain.task.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.keyresult.exception.KeyResultInvalidPositionException;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.keyresult.repository.KeyResultRepository;
import org.moonshot.server.domain.objective.dto.request.ModifyIndexRequestDto;
import org.moonshot.server.domain.objective.model.IndexService;
import org.moonshot.server.domain.task.dto.request.TaskCreateRequestDto;
import org.moonshot.server.domain.task.dto.request.TaskSingleCreateRequestDto;
import org.moonshot.server.domain.task.exception.TaskNotFoundException;
import org.moonshot.server.domain.task.exception.TaskNumberExceededException;
import org.moonshot.server.domain.task.model.Task;
import org.moonshot.server.domain.task.repository.TaskRepository;
import org.moonshot.server.domain.user.service.UserService;
import org.moonshot.server.global.auth.exception.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService implements IndexService {

    private static final int ACTIVE_TASK_NUMBER = 3;

    private final KeyResultRepository keyResultRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;

    public void createTask(TaskSingleCreateRequestDto request, Long userId) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(request.keyResultId())
                .orElseThrow();
        if (!keyResult.getObjective().getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }
        List<Task> taskList = taskRepository.findAllByKeyResultOrderByIdx(keyResult);

        if (taskList.size() >= ACTIVE_TASK_NUMBER) {
            throw new TaskNumberExceededException();
        }
        if (request.idx() > taskList.size()) {
            throw new KeyResultInvalidPositionException();
        }

        for (int i = request.idx(); i < taskList.size(); i++) {
            taskList.get(i).incrementIdx();
        }

        saveTask(keyResult, request);
    }

    public void saveTask(KeyResult keyResult, TaskSingleCreateRequestDto request) {
        taskRepository.save(Task.builder()
                .title(request.title())
                .idx(request.idx())
                .keyResult(keyResult)
                .build());
    }

    public void saveTask(KeyResult keyResult, TaskCreateRequestDto request) {
        if (!request.title().isEmpty()) {
            taskRepository.save(Task.builder()
                    .title(request.title())
                    .idx(request.idx())
                    .keyResult(keyResult)
                    .build());
        }
    }

    @Override
    public void modifyIdx(ModifyIndexRequestDto request, Long userId) {
        Task task = taskRepository.findTaskWithFetchJoin(request.id())
                .orElseThrow(TaskNotFoundException::new);
        userService.validateUserAuthorization(task.getKeyResult().getObjective().getUser(), userId);
        Integer prevIdx = task.getIdx();
        if (prevIdx.equals(request.idx())) {
            return;
        }
        List<Task> taskList = taskRepository.findAllByKeyResult(task.getKeyResult());

        task.modifyIdx(request.idx());
        if (prevIdx < request.idx()) {
            taskRepository.bulkUpdateTaskIdxDecrease(prevIdx + 1, request.idx(), task.getKeyResult().getId(), task.getId());
        } else {
            taskRepository.bulkUpdateTaskIdxIncrease(request.idx(), prevIdx, task.getKeyResult().getId(), task.getId());
        }
    }

}
