package org.moonshot.server.domain.task.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.keyresult.exception.KeyResultInvalidPositionException;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.keyresult.repository.KeyResultRepository;
import org.moonshot.server.domain.task.dto.request.TaskSingleCreateRequestDto;
import org.moonshot.server.domain.task.exception.TaskNumberExceededException;
import org.moonshot.server.domain.task.model.Task;
import org.moonshot.server.domain.task.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private static final int ACTIVE_TASK_NUMBER = 3;

    private final KeyResultRepository keyResultRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public void createTask(TaskSingleCreateRequestDto request) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(request.keyResultId())
                .orElseThrow();
        List<Task> taskList = taskRepository.findAllByKeyResultOrderByIdx(keyResult);

        if (taskList.size() >= ACTIVE_TASK_NUMBER) {
            throw new TaskNumberExceededException();
        }
        if (request.idx() > taskList.size()) {
            throw new KeyResultInvalidPositionException();
        }

        for (short i = request.idx(); i < taskList.size(); i++) {
            taskList.get(i).incrementIdx();
        }
        taskRepository.save(Task.builder()
                .title(request.title())
                .idx(request.idx())
                .keyResult(keyResult)
                .build());
    }

}
