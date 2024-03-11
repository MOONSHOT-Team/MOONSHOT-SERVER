package org.moonshot.log.service;

import static org.moonshot.keyresult.service.validator.KeyResultValidator.isKeyResultAchieved;
import static org.moonshot.log.service.validator.LogValidator.isCreateLog;
import static org.moonshot.log.service.validator.LogValidator.validateLogNum;
import static org.moonshot.response.ErrorType.NOT_FOUND_KEY_RESULT;
import static org.moonshot.user.service.validator.UserValidator.validateUserAuthorization;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.moonshot.exception.NotFoundException;
import org.moonshot.keyresult.dto.request.KeyResultCreateRequestDto;
import org.moonshot.keyresult.dto.request.KeyResultCreateRequestInfoDto;
import org.moonshot.keyresult.dto.request.KeyResultModifyRequestDto;
import org.moonshot.keyresult.model.KeyResult;
import org.moonshot.keyresult.repository.KeyResultRepository;
import org.moonshot.log.dto.request.LogCreateRequestDto;
import org.moonshot.log.dto.response.AchieveResponseDto;
import org.moonshot.log.dto.response.LogResponseDto;
import org.moonshot.log.model.Log;
import org.moonshot.log.model.LogState;
import org.moonshot.log.repository.LogRepository;
import org.moonshot.objective.model.Objective;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class LogService {

    private final KeyResultRepository keyResultRepository;
    private final LogRepository logRepository;

    public Optional<AchieveResponseDto> createRecordLog(final Long userId, final LogCreateRequestDto request) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(request.keyResultId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_KEY_RESULT));
        validateUserAuthorization(keyResult.getObjective().getUser().getId(), userId);
        Optional<Log> prevLog = logRepository.findLatestLogByKeyResultId(LogState.RECORD, request.keyResultId());
        long prevNum = -1;
        if (prevLog.isPresent()) {
            prevNum = prevLog.get().getCurrNum();
            validateLogNum(request.logNum(), prevNum);
        }
        Log log = logRepository.save(Log.builder()
                .date(LocalDateTime.now())
                .state(LogState.RECORD)
                .currNum(request.logNum())
                .prevNum(prevNum)
                .content(request.logContent())
                .keyResult(keyResult)
                .build());
        keyResult.modifyProgress(calculateKRProgressBar(log, keyResult.getTarget()));
        keyResult.getObjective().modifyProgress(calculateOProgressBar(keyResult.getObjective()));
        if (isKeyResultAchieved(keyResult.getObjective().getProgress())) {
            return Optional.of(AchieveResponseDto.of(keyResult.getObjective().getId(), keyResult.getObjective().getUser().getNickname(), calculateOProgressBar(keyResult.getObjective())));
        }
        return Optional.empty();
    }

    public Log createUpdateLog(final KeyResultModifyRequestDto request, final Long keyResultId) {
        KeyResult keyResult = keyResultRepository.findById(keyResultId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_KEY_RESULT));
        return logRepository.save(Log.builder()
                .date(LocalDateTime.now())
                .state(LogState.UPDATE)
                .currNum(request.target())
                .prevNum(keyResult.getTarget())
                .content(request.logContent())
                .keyResult(keyResult)
                .build());
    }

    public void createKRLog(final Object request, final Long keyResultId) {
        KeyResult keyResult = keyResultRepository.findById(keyResultId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_KEY_RESULT));

        if (request instanceof KeyResultCreateRequestInfoDto) {
            KeyResultCreateRequestInfoDto dto = (KeyResultCreateRequestInfoDto) request;
            logRepository.save(Log.builder()
                    .date(LocalDateTime.now())
                    .state(LogState.CREATE)
                    .currNum(dto.krTarget())
                    .content("")
                    .keyResult(keyResult)
                    .build());
        }
        if (request instanceof KeyResultCreateRequestDto) {
            KeyResultCreateRequestDto dto = (KeyResultCreateRequestDto) request;
            logRepository.save(Log.builder()
                    .date(LocalDateTime.now())
                    .state(LogState.CREATE)
                    .currNum(dto.target())
                    .content("")
                    .keyResult(keyResult)
                    .build());
        }
    }

    @Transactional(readOnly = true)
    public List<Log> getLogList(final KeyResult keyResult) {
        return logRepository.findAllByKeyResultOrderByIdDesc(keyResult);
    }

    @Transactional(readOnly = true)
    public List<LogResponseDto> getLogResponseDto(final List<Log> logList, final KeyResult keyResult) {
        return logList.stream()
                .map(log -> LogResponseDto.of(log.getState().getValue(),
                        log.getDate(),
                        setTitle(log.getPrevNum(), log.getCurrNum(), log, keyResult),
                        log.getContent()))
                .toList();
    }

    private String setTitle(final long prevNum, final long currNum, final Log log, final KeyResult keyResult) {
        if (isCreateLog(log.getState())) {
            return keyResult.getTitle() + " : " + keyResult.getTarget() + keyResult.getMetric();
        } else {
            return (prevNum == -1 ? "0" : NumberFormat.getNumberInstance().format(prevNum)) + keyResult.getMetric()
                    + " → " + NumberFormat.getNumberInstance().format(currNum) + keyResult.getMetric();
        }
    }

    public short calculateKRProgressBar(final Log log, final Long keyResultTarget) {
        return (log != null) ? (short) (Math.round(log.getCurrNum() / (double) keyResultTarget * 100)) : 0;
    }

    public short calculateOProgressBar(final Objective objective) {
        int totalKRProgress = 0;
        for (int i = 0; i < objective.getKeyResultList().size(); i++) {
            short krProgress = objective.getKeyResultList().get(i).getProgress();
            totalKRProgress += (krProgress >= 70) ? 100 : krProgress;
        }
        return (short) (totalKRProgress / objective.getKeyResultList().size());
    }

}
