package org.moonshot.keyresult.service;

import static org.moonshot.keyresult.service.validator.KeyResultValidator.*;
import static org.moonshot.log.service.validator.LogValidator.validateLogNum;
import static org.moonshot.task.service.validator.TaskValidator.validateTaskIndex;
import static org.moonshot.user.service.validator.UserValidator.validateUserAuthorization;
import static org.moonshot.validator.IndexValidator.isIndexIncreased;
import static org.moonshot.validator.IndexValidator.isSameIndex;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.moonshot.common.model.Period;
import org.moonshot.exception.keyresult.KeyResultNotFoundException;
import org.moonshot.exception.keyresult.KeyResultRequiredException;
import org.moonshot.exception.log.LogNotFoundException;
import org.moonshot.exception.objective.ObjectiveNotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class KeyResultService implements IndexService {

    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final LogService logService;
    private final LogRepository logRepository;

    public void createInitKRWithObjective(final Objective objective, final List<KeyResultCreateRequestInfoDto> requests) {
        List<KeyResultCreateRequestInfoDto> nonNullRequests = requests.stream().filter(Objects::nonNull).toList();
        for (int i = 0; i < nonNullRequests.size(); i++) {
            KeyResultCreateRequestInfoDto dto = nonNullRequests.get(i);
            validateKeyResultIndex(i, dto.idx());
            validateKRPeriodWithInObjPeriod(objective.getPeriod(), dto.startAt(), dto.expireAt());

            KeyResult keyResult = keyResultRepository.save(KeyResult.builder()
                    .title(dto.title())
                    .period(Period.of(dto.startAt(), dto.expireAt()))
                    .idx(dto.idx())
                    .target(dto.target())
                    .metric(dto.metric())
                    .objective(objective)
                    .build());
            logService.createKRLog(dto, keyResult.getId());

            if (hasKeyResultTask(dto.taskList())) {
                saveTasks(keyResult, dto.taskList());
            }
        }
    }

    public void createKeyResult(final KeyResultCreateRequestDto request, final Long userId) {
        Objective objective = objectiveRepository.findObjectiveAndUserById(request.objectiveId())
                .orElseThrow(ObjectiveNotFoundException::new);
        validateUserAuthorization(objective.getUser().getId(), userId);

        List<KeyResult> krList = keyResultRepository.findAllByObjective(objective);
        validateActiveKRSizeExceeded(krList.size());
        validateIndexUnderMaximum(request.idx(), krList.size());

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
        validateUserAuthorization(keyResult.getObjective().getUser().getId(), userId);

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


    public void deleteAllKeyResult(List<Objective> objectiveList) {
        List<KeyResult> keyResultList = keyResultRepository.findAllByObjectiveIn(objectiveList);
        logRepository.deleteAllInBatch(logRepository.findAllByKeyResultIn(keyResultList));
        taskRepository.deleteAllInBatch(taskRepository.findAllByKeyResultIn(keyResultList));
        keyResultRepository.deleteAllInBatch(keyResultList);
    }

    public Optional<AchieveResponseDto> modifyKeyResult(final KeyResultModifyRequestDto request, final Long userId) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(request.keyResultId())
                .orElseThrow(KeyResultNotFoundException::new);
        validateUserAuthorization(keyResult.getObjective().getUser().getId(), userId);

        if (hasChange(request.title())) {
            keyResult.modifyTitle(request.title());
        }

        if (hasChange(request.state())) {
            keyResult.modifyState(request.state());
            return Optional.empty();
        }

        if (hasDateChange(request.startAt(), request.expireAt())) {
            LocalDate newStartAt = getLatestDate(request.startAt(), keyResult.getPeriod().getStartAt());
            LocalDate newExpireAt = getLatestDate(request.expireAt(), keyResult.getPeriod().getExpireAt());
            validateKeyResultPeriod(keyResult.getObjective().getPeriod(), newStartAt, newExpireAt);

            keyResult.modifyPeriod(Period.of(newStartAt, newExpireAt));
            return Optional.empty();
        }

        if (request.target() == null || request.logContent() == null){
            throw new KeyResultRequiredException();
        }

        Log updateLog = logService.createUpdateLog(request, keyResult.getId());
        validateLogNum(request.target(), updateLog.getKeyResult().getTarget());

        Optional<Log> prevLog = logRepository.findLatestLogByKeyResultId(LogState.RECORD, request.keyResultId());
        keyResult.modifyTarget(request.target());
        if(prevLog.isPresent()) {
            keyResult.modifyProgress(logService.calculateKRProgressBar(prevLog.get(), keyResult.getTarget()));
        }
        short progress = logService.calculateOProgressBar(keyResult.getObjective());
        keyResult.getObjective().modifyProgress(progress);

        if (isKeyResultAchieved(keyResult.getObjective().getProgress())) {
            return Optional.of(AchieveResponseDto.of(keyResult.getObjective().getId(), keyResult.getObjective().getUser().getNickname(), progress));
        }

        return Optional.empty();
    }

    @Override
    public void modifyIdx(final ModifyIndexRequestDto request, final Long userId) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(request.id())
                .orElseThrow(KeyResultNotFoundException::new);
        validateUserAuthorization(keyResult.getObjective().getUser().getId(), userId);

        Long krCount = keyResultRepository.countAllByObjectiveId(keyResult.getObjective().getId());
        validateIndex(krCount, request.idx());

        Integer prevIdx = keyResult.getIdx();
        if (isSameIndex(prevIdx, request.idx())) {
            return;
        }

        keyResult.modifyIdx(request.idx());
        if (isIndexIncreased(prevIdx, request.idx())) {
            keyResultRepository.bulkUpdateIdxDecrease(prevIdx + 1, request.idx(), keyResult.getObjective().getId(), keyResult.getId());
        } else {
            keyResultRepository.bulkUpdateIdxIncrease(request.idx(), prevIdx, keyResult.getObjective().getId(), keyResult.getId());
        }
    }

    @Transactional(readOnly = true)
    public KRDetailResponseDto getKRDetails(final Long userId, final Long keyResultId) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(keyResultId)
                .orElseThrow(KeyResultNotFoundException::new);
        validateUserAuthorization(keyResult.getObjective().getUser().getId(), userId);

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
                logService.calculateKRProgressBar(target, keyResult.getTarget()),
                keyResult.getState().getValue(),
                keyResult.getPeriod().getStartAt(),
                keyResult.getPeriod().getExpireAt(),
                logService.getLogResponseDto(logList, keyResult));
    }

    private void saveTasks(final KeyResult keyResult, final List<TaskCreateRequestDto> taskList) {
        List<TaskCreateRequestDto> nonNullTaskList = taskList.stream().filter(Objects::nonNull).toList();
        for (int i = 0; i < nonNullTaskList.size(); i++) {
            TaskCreateRequestDto taskDto = nonNullTaskList.get(i);
            validateTaskIndex(i, taskDto.idx());
            taskService.saveTask(keyResult, taskDto);
        }
    }

    private LocalDate getLatestDate(LocalDate requestDate, LocalDate originDate) {
        return (requestDate != null) ? requestDate : originDate;
    }

}
