package org.moonshot.server.domain.keyresult.service;

import java.time.LocalDate;
import java.util.List;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultCreateRequestDto;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultCreateRequestInfoDto;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultModifyRequestDto;
import org.moonshot.server.domain.keyresult.dto.response.KRDetailResponseDto;
import org.moonshot.server.domain.keyresult.exception.*;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.keyresult.repository.KeyResultRepository;
import org.moonshot.server.domain.log.dto.response.AchieveResponseDto;
import org.moonshot.server.domain.log.exception.InvalidLogValueException;
import org.moonshot.server.domain.log.exception.LogNotFoundException;
import org.moonshot.server.domain.log.model.Log;
import org.moonshot.server.domain.log.model.LogState;
import org.moonshot.server.domain.objective.dto.request.ModifyIndexRequestDto;
import org.moonshot.server.domain.log.repository.LogRepository;
import org.moonshot.server.domain.log.service.LogService;
import org.moonshot.server.domain.objective.exception.ObjectiveNotFoundException;
import org.moonshot.server.domain.objective.model.IndexService;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.domain.objective.repository.ObjectiveRepository;
import org.moonshot.server.domain.task.dto.request.TaskCreateRequestDto;
import org.moonshot.server.domain.task.repository.TaskRepository;
import org.moonshot.server.domain.task.service.TaskService;
import org.moonshot.server.domain.user.service.UserService;
import org.moonshot.server.global.common.model.Period;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class KeyResultService implements IndexService {

    private static final int ACTIVE_KEY_RESULT_NUMBER = 3;

    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final LogService logService;
    private final LogRepository logRepository;

    public void createInitKRWithObjective(Objective objective, List<KeyResultCreateRequestInfoDto> requests) {
        for (KeyResultCreateRequestInfoDto dto : requests) {
            if (dto.startAt().isBefore(objective.getPeriod().getStartAt()) ||
                    dto.expireAt().isAfter(objective.getPeriod().getExpireAt()) ||
                    dto.startAt().isAfter(objective.getPeriod().getExpireAt()) ||
                    dto.startAt().isAfter(dto.expireAt())) {
                throw new KeyResultInvalidPeriodException();
            }
            KeyResult keyResult = keyResultRepository.save(KeyResult.builder()
                    .title(dto.title())
                    .period(Period.of(dto.startAt(), dto.expireAt()))
                    .idx(dto.idx())
                    .target(dto.target())
                    .metric(dto.metric())
                    .objective(objective)
                    .build());
            logService.createKRLog(dto, keyResult.getId());
            if (dto.taskList() != null) {
                for (TaskCreateRequestDto taskDto : dto.taskList()) {
                    taskService.saveTask(keyResult, taskDto);
                }
            }
        }
    }

    public void createKeyResult(KeyResultCreateRequestDto request, Long userId) {
        Objective objective = objectiveRepository.findObjectiveAndUserById(request.objectiveId())
                .orElseThrow(ObjectiveNotFoundException::new);
        userService.validateUserAuthorization(objective.getUser(), userId);

        List<KeyResult> krList = keyResultRepository.findAllByObjective(objective);
        if (krList.size() >= ACTIVE_KEY_RESULT_NUMBER) {
            throw new KeyResultNumberExceededException();
        }
        if (request.idx() > krList.size()) {
            throw new KeyResultInvalidIndexException();
        }

        for (int i = request.idx(); i < krList.size(); i++) {
            krList.get(i).incrementIdx();
        }
        KeyResult keyResult = keyResultRepository.save(KeyResult.builder()
                .objective(objective)
                .title(request.title())
                .period(Period.of(request.startAt(), request.expireAt()))
                .idx(request.idx())
                .target(request.target())
                .metric(request.metric()).build());
      logService.createKRLog(request, keyResult.getId());
    }

    public void deleteKeyResult(Long keyResultId, Long userId) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(keyResultId)
                .orElseThrow(KeyResultNotFoundException::new);
        userService.validateUserAuthorization(keyResult.getObjective().getUser(), userId);

        logRepository.deleteAllInBatch(logRepository.findAllByKeyResult(keyResult));
        taskRepository.deleteAllInBatch(taskRepository.findAllByKeyResult(keyResult));
        keyResultRepository.delete(keyResult);
    }

    public void deleteKeyResult(Objective objective) {
        cascadeDelete(keyResultRepository.findAllByObjective(objective));
    }

    private void cascadeDelete(List<KeyResult> krList) {
        krList.forEach((kr) -> {
            logRepository.deleteAllInBatch(logRepository.findAllByKeyResult(kr));
            taskRepository.deleteAllInBatch(kr.getTaskList());
        });
        keyResultRepository.deleteAllInBatch(krList);
    }

    public Optional<AchieveResponseDto> modifyKeyResult(KeyResultModifyRequestDto request, Long userId) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(request.keyResultId())
                .orElseThrow(KeyResultNotFoundException::new);
        userService.validateUserAuthorization(keyResult.getObjective().getUser(), userId);

        if (request.title() != null) {
            keyResult.modifyTitle(request.title());
        }
        if (request.startAt() != null || request.expireAt() != null) {
            LocalDate newStartAt = (request.startAt() != null) ? request.startAt() : keyResult.getPeriod().getStartAt();
            LocalDate newExpireAt = (request.expireAt() != null) ? request.expireAt() : keyResult.getPeriod().getExpireAt();
            keyResult.modifyPeriod(Period.of(newStartAt, newExpireAt));
        }
        if (request.target() == null || request.logContent() == null){
            throw new KeyResultRequiredException();
        }
        Log updateLog = logService.createUpdateLog(request, keyResult.getId());
        if (request.target().equals(updateLog.getKeyResult().getTarget())) {
            throw new InvalidLogValueException();
        }
        Log prevLog = logRepository.findLatestLogByKeyResultId(LogState.UPDATE, request.keyResultId())
                .orElseThrow(LogNotFoundException::new);
        keyResult.modifyTarget(request.target());
        keyResult.modifyProgress(logService.calculateKRProgressBar(prevLog, keyResult));
        short progress = logService.calculateOProgressBar(keyResult.getObjective());
        keyResult.getObjective().modifyProgress(progress);
        if (keyResult.getObjective().getProgress() == 100) {
            return Optional.of(AchieveResponseDto.of(keyResult.getObjective().getId(), keyResult.getObjective().getUser().getNickname(), progress));
        }
        if (request.state() != null) {
            keyResult.modifyState(request.state());
        }
        return Optional.empty();
    }

    @Override
    public void modifyIdx(ModifyIndexRequestDto request, Long userId) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(request.id())
                .orElseThrow(KeyResultNotFoundException::new);
        userService.validateUserAuthorization(keyResult.getObjective().getUser(), userId);

        Long krCount = keyResultRepository.countAllByObjectiveId(keyResult.getObjective().getId());
        if (isInvalidIdx(krCount, request.idx())) {
            throw new KeyResultInvalidIndexException();
        }

        Integer prevIdx = keyResult.getIdx();
        if (prevIdx.equals(request.idx())) {
            return;
        }

        keyResult.modifyIdx(request.idx());
        if (prevIdx < request.idx()) {
            keyResultRepository.bulkUpdateIdxDecrease(prevIdx + 1, request.idx(), keyResult.getObjective().getId(), keyResult.getId());
        } else {
            keyResultRepository.bulkUpdateIdxIncrease(request.idx(), prevIdx, keyResult.getObjective().getId(), keyResult.getId());
        }
    }

    @Transactional(readOnly = true)
    public KRDetailResponseDto getKRDetails(Long userId, Long keyResultId) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(keyResultId)
                .orElseThrow(KeyResultNotFoundException::new);
        userService.validateUserAuthorization(keyResult.getObjective().getUser(), userId);
        List<Log> logList = logService.getLogList(keyResult);
        Log target = null;
        for (Log log : logList) {
            if (log.getState().equals(LogState.RECORD)) {
                target = log;
                break;
            }
        }
        return KRDetailResponseDto.of(keyResult.getTitle(),
                keyResult.getTarget(),
                keyResult.getMetric(),
                logService.calculateKRProgressBar(target, keyResult),
                keyResult.getState().getValue(),
                keyResult.getPeriod().getStartAt(),
                keyResult.getPeriod().getExpireAt(),
                logService.getLogResponseDto(logList, keyResult));
    }

    private boolean isInvalidIdx(Long keyResultCount, int idx) {
        return (keyResultCount <= idx) || (idx < 0);
    }

}
