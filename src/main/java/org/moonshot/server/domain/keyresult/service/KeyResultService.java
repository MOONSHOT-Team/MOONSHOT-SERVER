package org.moonshot.server.domain.keyresult.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultCreateRequestDto;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultCreateRequestInfoDto;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultModifyRequestDto;
import org.moonshot.server.domain.keyresult.exception.KeyResultInvalidPositionException;
import org.moonshot.server.domain.keyresult.exception.KeyResultNotFoundException;
import org.moonshot.server.domain.keyresult.exception.KeyResultNumberExceededException;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.keyresult.repository.KeyResultRepository;
import org.moonshot.server.domain.log.repository.LogRepository;
import org.moonshot.server.domain.log.service.LogService;
import org.moonshot.server.domain.objective.exception.ObjectiveNotFoundException;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.domain.objective.repository.ObjectiveRepository;
import org.moonshot.server.domain.task.dto.request.TaskCreateRequestDto;
import org.moonshot.server.domain.task.model.Task;
import org.moonshot.server.domain.task.repository.TaskRepository;
import org.moonshot.server.domain.task.service.TaskService;
import org.moonshot.server.domain.user.service.UserService;
import org.moonshot.server.global.common.model.Period;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KeyResultService {

    private static final int ACTIVE_KEY_RESULT_NUMBER = 3;

    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final LogService logService;
    private final LogRepository logRepository;

    //TODO
    // 여기 모든 로직에 User 관련 기능이 추가된 이후
    // KeyResult의 소유자인지 확인하는 절차를 추가해야 함.

    @Transactional
    public void createInitKRWithObjective(Objective objective, List<KeyResultCreateRequestInfoDto> requests) {
        for (KeyResultCreateRequestInfoDto dto : requests) {
            KeyResult keyResult = keyResultRepository.save(KeyResult.builder()
                    .title(dto.title())
                    .period(Period.of(dto.startAt(), dto.expireAt()))
                    .idx(dto.idx())
                    .target(dto.target())
                    .metric(dto.metric())
                    .descriptionBefore(dto.descriptionBefore())
                    .descriptionAfter(dto.descriptionAfter())
                    .objective(objective)
                    .build());
            logService.createKRLog(dto, keyResult.getId());
            if (dto.taskList() != null) {
                taskRepository.saveAll(dto.taskList().stream().map((task) -> Task.builder()
                        .title(task.title())
                        .idx(task.idx())
                        .keyResult(keyResult)
                        .build()).toList());
                for (TaskCreateRequestDto taskDto : dto.taskList()) {
                    taskService.saveTask(keyResult, taskDto);
                }
            }
        }
    }

    @Transactional
    public void createKeyResult(KeyResultCreateRequestDto request, Long userId) {
        Objective objective = objectiveRepository.findObjectiveAndUserById(request.objectiveId())
                .orElseThrow(ObjectiveNotFoundException::new);
        userService.validateUserAuthorization(objective.getUser(), userId);

        List<KeyResult> krList = keyResultRepository.findAllByObjective(objective);
        if (krList.size() >= ACTIVE_KEY_RESULT_NUMBER) {
            throw new KeyResultNumberExceededException();
        }
        if (request.idx() > krList.size()) {
            throw new KeyResultInvalidPositionException();
        }

        for (short i = request.idx(); i < krList.size(); i++) {
            krList.get(i).incrementIdx();
        }
        KeyResult keyResult = keyResultRepository.save(KeyResult.builder()
                .objective(objective)
                .title(request.title())
                .period(Period.of(request.startAt(), request.expireAt()))
                .idx(request.idx())
                .target(request.target())
                .metric(request.metric())
                .descriptionBefore(request.descriptionBefore())
                .descriptionAfter(request.descriptionAfter()).build());
      logService.createKRLog(request, keyResult.getId());
    }

    @Transactional
    public void deleteKeyResult(Long keyResultId, Long userId) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(keyResultId)
                .orElseThrow(KeyResultNotFoundException::new);
        userService.validateUserAuthorization(keyResult.getObjective().getUser(), userId);

        logRepository.deleteAllInBatch(logRepository.findAllByKeyResult(keyResult));
        taskRepository.deleteAllInBatch(taskRepository.findAllByKeyResult(keyResult));
        keyResultRepository.delete(keyResult);
    }

    @Transactional
    public void deleteKeyResult(Objective objective) {
        cascadeDelete(keyResultRepository.findAllByObjective(objective));
    }

    private void cascadeDelete(List<KeyResult> krList) {
        krList.forEach((kr) -> taskRepository.deleteAllInBatch(taskRepository.findAllByKeyResult(kr)));
        keyResultRepository.deleteAllInBatch(krList);
    }

    @Transactional
    public void modifyKeyResult(KeyResultModifyRequestDto request, Long userId) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(request.keyResultId())
                .orElseThrow(KeyResultNotFoundException::new);
        userService.validateUserAuthorization(keyResult.getObjective().getUser(), userId);

        if (request.title() != null) {
            keyResult.modifyTitle(request.title());
        }
        if (request.startAt() != null || request.expireAt() != null) {
            LocalDateTime newStartAt = (request.startAt() != null) ? request.startAt() : keyResult.getPeriod().getStartAt();
            LocalDateTime newExpireAt = (request.expireAt() != null) ? request.expireAt() : keyResult.getPeriod().getExpireAt();
            keyResult.modifyPeriod(Period.of(newStartAt, newExpireAt));
        }
        if (request.target() != null) {
            if (request.logContent() !=  null) {
                logService.createUpdateLog(request, keyResult.getId());
            }
            keyResult.modifyTarget(request.target());
        }
        if (request.state() != null) {
            keyResult.modifyState(request.state());
        }

    }

}
