package org.moonshot.task.service;

import static org.moonshot.response.ErrorType.NOT_FOUND_KEY_RESULT;
import static org.moonshot.response.ErrorType.NOT_FOUND_TASK;
import static org.moonshot.task.service.validator.TaskValidator.validateActiveTaskSizeExceeded;
import static org.moonshot.task.service.validator.TaskValidator.validateIndex;
import static org.moonshot.task.service.validator.TaskValidator.validateIndexUnderMaximum;
import static org.moonshot.user.service.validator.UserValidator.validateUserAuthorization;
import static org.moonshot.validator.IndexValidator.isIndexIncreased;
import static org.moonshot.validator.IndexValidator.isSameIndex;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moonshot.exception.NotFoundException;
import org.moonshot.keyresult.model.KeyResult;
import org.moonshot.keyresult.repository.KeyResultRepository;
import org.moonshot.objective.dto.request.ModifyIndexRequestDto;
import org.moonshot.objective.model.IndexService;
import org.moonshot.task.dto.request.TaskCreateRequestDto;
import org.moonshot.task.dto.request.TaskSingleCreateRequestDto;
import org.moonshot.task.model.Task;
import org.moonshot.task.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService implements IndexService {

    private final KeyResultRepository keyResultRepository;
    private final TaskRepository taskRepository;
    public void createTask(final TaskSingleCreateRequestDto request, final Long userId) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(request.keyResultId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_KEY_RESULT));
        validateUserAuthorization(keyResult.getObjective().getUser().getId(), userId);

        List<Task> taskList = taskRepository.findAllByKeyResultOrderByIdx(keyResult);
        validateActiveTaskSizeExceeded(taskList.size());
        validateIndexUnderMaximum(request.taskIdx(), taskList.size());

        taskRepository.bulkUpdateTaskIdxIncrease(request.taskIdx(), taskList.size(), keyResult.getId(), -1L);

        saveTask(keyResult, request);
    }

    public void saveTask(final KeyResult keyResult, final TaskSingleCreateRequestDto request) {
        taskRepository.save(Task.builder()
                .title(request.taskTitle())
                .idx(request.taskIdx())
                .keyResult(keyResult)
                .build());
    }

    public void saveTask(final KeyResult keyResult, final TaskCreateRequestDto request) {
        if (!request.taskTitle().isEmpty()) {
            taskRepository.save(Task.builder()
                    .title(request.taskTitle())
                    .idx(request.taskIdx())
                    .keyResult(keyResult)
                    .build());
        }
    }

    @Override
    public void modifyIdx(final ModifyIndexRequestDto request, final Long userId) {
        Task task = taskRepository.findTaskWithFetchJoin(request.id())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_TASK));
        validateUserAuthorization(task.getKeyResult().getObjective().getUser().getId(), userId);

        Long taskCount = taskRepository.countAllByKeyResultId(task.getKeyResult().getId());
        validateIndex(taskCount, request.idx());
        Integer prevIdx = task.getIdx();
        if (isSameIndex(prevIdx, request.idx())) {
            return;
        }

        task.modifyIdx(request.idx());
        if (isIndexIncreased(prevIdx, request.idx())) {
            taskRepository.bulkUpdateTaskIdxDecrease(prevIdx + 1, request.idx(), task.getKeyResult().getId(), task.getId());
        } else {
            taskRepository.bulkUpdateTaskIdxIncrease(request.idx(), prevIdx, task.getKeyResult().getId(), task.getId());
        }
    }

    public void deleteTask(final Long userId, Long taskId) {
        Task task = taskRepository.findTaskWithFetchJoin(taskId)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_TASK));
        validateUserAuthorization(task.getKeyResult().getObjective().getUser().getId(), userId);

        taskRepository.deleteById(taskId);
        taskRepository.bulkUpdateTaskIdxDecrease(task.getIdx(), 3, task.getKeyResult().getId(), -1L);
    }
    
}
