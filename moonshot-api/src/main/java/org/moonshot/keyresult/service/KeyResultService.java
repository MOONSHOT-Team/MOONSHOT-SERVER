package org.moonshot.keyresult.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.moonshot.common.model.Period;
import org.moonshot.exception.keyresult.KeyResultInvalidIndexException;
import org.moonshot.exception.keyresult.KeyResultInvalidPeriodException;
import org.moonshot.exception.keyresult.KeyResultNumberExceededException;
import org.moonshot.exception.keyresult.KeyResultRequiredException;
import org.moonshot.exception.keyresult.KeyResultNotFoundException;
import org.moonshot.exception.log.LogNotFoundException;
import org.moonshot.exception.objective.ObjectiveNotFoundException;
import org.moonshot.exception.log.InvalidLogValueException;
import org.moonshot.exception.task.TaskInvalidIndexException;
import org.moonshot.keyresult.dto.request.KeyResultCreateRequestDto;
import org.moonshot.keyresult.dto.request.KeyResultCreateRequestInfoDto;
import org.moonshot.keyresult.dto.request.KeyResultModifyRequestDto;
import org.moonshot.keyresult.dto.response.KRDetailResponseDto;
import org.moonshot.keyresult.model.KeyResult;
import org.moonshot.keyresult.repository.KeyResultRepository;
import org.moonshot.log.dto.response.AchieveResponseDto;
import org.moonshot.log.model.Log;
import org.moonshot.log.model.LogState;
import org.moonshot.log.repository.LogRepository;
import org.moonshot.log.service.LogService;
import org.moonshot.objective.dto.request.ModifyIndexRequestDto;
import org.moonshot.objective.model.IndexService;
import org.moonshot.objective.model.Objective;
import org.moonshot.objective.repository.ObjectiveRepository;
import org.moonshot.task.dto.request.TaskCreateRequestDto;
import org.moonshot.task.repository.TaskRepository;
import org.moonshot.task.service.TaskService;
import org.moonshot.user.service.UserService;
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

    public void createInitKRWithObjective(final Objective objective, final List<KeyResultCreateRequestInfoDto> requests) {
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i) == null) {
                return;
            }
            KeyResultCreateRequestInfoDto dto = requests.get(i);
            if (i != dto.idx()) {
                throw new KeyResultInvalidIndexException();
            }
            isKRWithInObjective(objective, dto);

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
                saveTasks(keyResult, dto.taskList());
            }
        }
    }

    public void createKeyResult(final KeyResultCreateRequestDto request, final Long userId) {
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

    public void deleteKeyResult(final Long keyResultId, final Long userId) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(keyResultId)
                .orElseThrow(KeyResultNotFoundException::new);
        userService.validateUserAuthorization(keyResult.getObjective().getUser(), userId);

        logRepository.deleteAllInBatch(logRepository.findAllByKeyResult(keyResult));
        taskRepository.deleteAllInBatch(taskRepository.findAllByKeyResult(keyResult));
        keyResultRepository.delete(keyResult);
    }

    public void deleteKeyResult(final Objective objective) {
        cascadeDelete(keyResultRepository.findAllByObjective(objective));
    }

    private void cascadeDelete(List<KeyResult> krList) {
        krList.forEach((kr) -> {
            logRepository.deleteAllInBatch(logRepository.findAllByKeyResult(kr));
            taskRepository.deleteAllInBatch(kr.getTaskList());
        });
        keyResultRepository.deleteAllInBatch(krList);
    }

    public Optional<AchieveResponseDto> modifyKeyResult(final KeyResultModifyRequestDto request, final Long userId) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(request.keyResultId())
                .orElseThrow(KeyResultNotFoundException::new);
        userService.validateUserAuthorization(keyResult.getObjective().getUser(), userId);

        if (request.title() != null) {
            keyResult.modifyTitle(request.title());
        }
        if (request.state() == null && (request.startAt() != null || request.expireAt() != null)) {
            LocalDate newStartAt = (request.startAt() != null) ? request.startAt() : keyResult.getPeriod().getStartAt();
            LocalDate newExpireAt = (request.expireAt() != null) ? request.expireAt() : keyResult.getPeriod().getExpireAt();
            if(!(isValidKeyResultPeriod(keyResult, newStartAt, newExpireAt))) {
                throw new KeyResultInvalidPeriodException();
            }
            keyResult.modifyPeriod(Period.of(newStartAt, newExpireAt));
            return Optional.empty();
        }
        if ((request.state() == null && request.target() == null) || (request.state() == null && request.logContent() == null)){
            throw new KeyResultRequiredException();
        }
        if (request.state() == null) {
            Log updateLog = logService.createUpdateLog(request, keyResult.getId());
            if (request.target().equals(updateLog.getKeyResult().getTarget())) {
                throw new InvalidLogValueException();
            }
            Log prevLog = logRepository.findLatestLogByKeyResultId(LogState.RECORD, request.keyResultId())
                    .orElseThrow(LogNotFoundException::new);
            keyResult.modifyTarget(request.target());
            keyResult.modifyProgress(logService.calculateKRProgressBar(prevLog, keyResult));
            short progress = logService.calculateOProgressBar(keyResult.getObjective());
            keyResult.getObjective().modifyProgress(progress);
            if (keyResult.getObjective().getProgress() == 100) {
                return Optional.of(AchieveResponseDto.of(keyResult.getObjective().getId(), keyResult.getObjective().getUser().getNickname(), progress));
            }
        } else {
            keyResult.modifyState(request.state());
        }
        return Optional.empty();
    }

    @Override
    public void modifyIdx(final ModifyIndexRequestDto request, final Long userId) {
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
    public KRDetailResponseDto getKRDetails(final Long userId, final Long keyResultId) {
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

    private boolean isValidKeyResultPeriod(final KeyResult keyResult, final LocalDate newStartAt, final LocalDate newExpireAt) {
        if (newStartAt.isAfter(newExpireAt)) {
            return false;
        }
        if (newStartAt.isBefore(keyResult.getObjective().getPeriod().getStartAt()) || newStartAt.isAfter(keyResult.getObjective().getPeriod().getExpireAt())) {
            return false;
        }
        if (newExpireAt.isBefore(newStartAt)) {
            return false;
        }
        if (newExpireAt.isBefore(keyResult.getObjective().getPeriod().getStartAt()) || newExpireAt.isAfter(keyResult.getObjective().getPeriod().getExpireAt())) {
            return false;
        }
        return true;
    }
    private boolean isInvalidIdx(final Long keyResultCount, final int idx) {
        return (keyResultCount <= idx) || (idx < 0);
    }

    private void isKRWithInObjective(final Objective objective, final KeyResultCreateRequestInfoDto dto) {
        if (dto.startAt().isBefore(objective.getPeriod().getStartAt()) ||
                dto.expireAt().isAfter(objective.getPeriod().getExpireAt()) ||
                dto.startAt().isAfter(objective.getPeriod().getExpireAt()) ||
                dto.startAt().isAfter(dto.expireAt())) {
            throw new KeyResultInvalidPeriodException();
        }
    }

    private void saveTasks(final KeyResult keyResult, final List<TaskCreateRequestDto> taskList) {
        for (int i = 0; i < taskList.size(); i++) {
            TaskCreateRequestDto taskDto = taskList.get(i);
            if (i != taskDto.idx()) {
                throw new TaskInvalidIndexException();
            }
            taskService.saveTask(keyResult, taskDto);
        }
    }

}