package org.moonshot.task.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moonshot.exception.keyresult.KeyResultInvalidIndexException;
import org.moonshot.exception.task.TaskNumberExceededException;
import org.moonshot.exception.global.auth.AccessDeniedException;
import org.moonshot.exception.task.TaskNotFoundException;
import org.moonshot.keyresult.model.KeyResult;
import org.moonshot.keyresult.repository.KeyResultRepository;
import org.moonshot.objective.dto.request.ModifyIndexRequestDto;
import org.moonshot.objective.model.IndexService;
import org.moonshot.task.dto.request.TaskCreateRequestDto;
import org.moonshot.task.dto.request.TaskSingleCreateRequestDto;
import org.moonshot.task.model.Task;
import org.moonshot.task.repository.TaskRepository;
import org.moonshot.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService implements IndexService {

    private static final int ACTIVE_TASK_NUMBER = 3;

    private final KeyResultRepository keyResultRepository;
    private final TaskRepository taskRepository;
//    private final UserService userService;

    public void createTask(final TaskSingleCreateRequestDto request, final Long userId) {
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
            throw new KeyResultInvalidIndexException();
        }

        for (int i = request.idx(); i < taskList.size(); i++) {
            taskList.get(i).incrementIdx();
        }
        saveTask(keyResult, request);
    }

    public void saveTask(final KeyResult keyResult, final TaskSingleCreateRequestDto request) {
        taskRepository.save(Task.builder()
                .title(request.title())
                .idx(request.idx())
                .keyResult(keyResult)
                .build());
    }

    public void saveTask(final KeyResult keyResult, final TaskCreateRequestDto request) {
        if (!request.title().isEmpty()) {
            taskRepository.save(Task.builder()
                    .title(request.title())
                    .idx(request.idx())
                    .keyResult(keyResult)
                    .build());
        }
    }

    @Override
    public void modifyIdx(final ModifyIndexRequestDto request, final Long userId) {
        Task task = taskRepository.findTaskWithFetchJoin(request.id())
                .orElseThrow(TaskNotFoundException::new);
        if (task.getKeyResult().getObjective().getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }
        Long taskCount = taskRepository.countAllByKeyResultId(task.getKeyResult().getId());
        if (isInvalidIdx(taskCount, request.idx())) {
            throw new KeyResultInvalidIndexException();
        }
        Integer prevIdx = task.getIdx();
        if (prevIdx.equals(request.idx())) {
            return;
        }

        task.modifyIdx(request.idx());
        if (prevIdx < request.idx()) {
            taskRepository.bulkUpdateTaskIdxDecrease(prevIdx + 1, request.idx(), task.getKeyResult().getId(), task.getId());
        } else {
            taskRepository.bulkUpdateTaskIdxIncrease(request.idx(), prevIdx, task.getKeyResult().getId(), task.getId());
        }
    }

    private boolean isInvalidIdx(final Long taskCount, final int idx) {
        return (taskCount <= idx) || (idx < 0);
    }

    public void deleteTask(final Long userId, Long taskId) {
        Task task = taskRepository.findTaskWithFetchJoin(taskId)
                        .orElseThrow(TaskNotFoundException::new);
        if (task.getKeyResult().getObjective().getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }
        taskRepository.deleteById(taskId);
    }
    
}
