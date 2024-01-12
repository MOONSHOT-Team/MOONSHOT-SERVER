package org.moonshot.server.domain.log.service;

import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultCreateRequestDto;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultCreateRequestInfoDto;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultModifyRequestDto;
import org.moonshot.server.domain.keyresult.exception.KeyResultNotFoundException;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.keyresult.repository.KeyResultRepository;
import org.moonshot.server.domain.log.dto.request.LogCreateRequestDto;
import org.moonshot.server.domain.log.dto.response.AchieveResponseDto;
import org.moonshot.server.domain.log.dto.response.LogResponseDto;
import org.moonshot.server.domain.log.exception.InvalidLogValueException;
import org.moonshot.server.domain.log.model.Log;
import org.moonshot.server.domain.log.model.LogState;
import org.moonshot.server.domain.log.repository.LogRepository;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.global.auth.exception.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LogService {

    private final KeyResultRepository keyResultRepository;
    private final LogRepository logRepository;

    @Transactional
    public Optional<AchieveResponseDto> createRecordLog(Long userId, LogCreateRequestDto request) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(request.keyResultId())
                .orElseThrow(KeyResultNotFoundException::new);
        if (!keyResult.getObjective().getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }
        Optional<Log> prevLog = logRepository.findLatestLogByKeyResultId(LogState.RECORD, request.keyResultId());
        long prevNum = -1;
        if (!prevLog.isEmpty()) {
            prevNum = prevLog.get().getCurrNum();
            if(request.logNum() == prevNum) {
                throw new InvalidLogValueException();
            }
        }
        Log log = logRepository.save(Log.builder()
                .date(LocalDateTime.now())
                .state(LogState.RECORD)
                .currNum(request.logNum())
                .prevNum(prevNum)
                .content(request.logContent())
                .keyResult(keyResult)
                .build());
        keyResult.modifyProgress(calculateKRProgressBar(log, keyResult));
        keyResult.getObjective().modifyProgress(calculateOProgressBar(keyResult.getObjective()));
        if (keyResult.getObjective().getProgress() == 100) {
            return Optional.of(AchieveResponseDto.of(keyResult.getObjective().getId(), keyResult.getObjective().getUser().getNickname(), calculateOProgressBar(keyResult.getObjective())));
        }
        return Optional.empty();
    }

    @Transactional
    public Log createUpdateLog(KeyResultModifyRequestDto request, Long keyResultId) {
        KeyResult keyResult = keyResultRepository.findById(keyResultId)
                .orElseThrow(KeyResultNotFoundException::new);
        return logRepository.save(Log.builder()
                .date(LocalDateTime.now())
                .state(LogState.UPDATE)
                .currNum(request.target())
                .prevNum(keyResult.getTarget())
                .content(request.logContent())
                .keyResult(keyResult)
                .build());
    }

    @Transactional
    public void createKRLog(Object request, Long keyResultId) {
        KeyResult keyResult = keyResultRepository.findById(keyResultId)
                .orElseThrow(KeyResultNotFoundException::new);

        if (request instanceof KeyResultCreateRequestInfoDto) {
            KeyResultCreateRequestInfoDto dto = (KeyResultCreateRequestInfoDto) request;
            logRepository.save(Log.builder()
                    .date(LocalDateTime.now())
                    .state(LogState.CREATE)
                    .currNum(dto.target())
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

    public List<Log> getLogList(KeyResult keyResult) {
        return logRepository.findAllByKeyResultOrderByIdDesc(keyResult);
    }

    public List<LogResponseDto> getLogResponseDto(List<Log> logList, KeyResult keyResult) {
        return logList.stream()
                .map(log -> LogResponseDto.of(log.getState().getValue(),
                        log.getDate(),
                        setTitle(log.getPrevNum(), log.getCurrNum(), log, keyResult),
                        log.getContent()))
                .toList();
    }

    public String setTitle(long prevNum, long currNum, Log log, KeyResult keyResult) {
        if (log.getState() == LogState.CREATE) {
            return keyResult.getDescriptionBefore() + " " + keyResult.getTarget() + keyResult.getMetric() + " " + keyResult.getDescriptionAfter();
        } else {
            return (prevNum == -1 ? "0" : NumberFormat.getNumberInstance().format(prevNum)) + keyResult.getMetric()
                    + " → " + NumberFormat.getNumberInstance().format(currNum) + keyResult.getMetric();
        }
    }

    public short calculateKRProgressBar(Log log, KeyResult keyResult) {
        return (log != null) ? (short) (Math.round(log.getCurrNum() / (double) keyResult.getTarget() * 100)) : 0;
    }

    public short calculateOProgressBar(Objective objective) {
        int totalKRProgress = 0;
        for (int i = 0; i < objective.getKeyResultList().size(); i++) {
            short krProgress = objective.getKeyResultList().get(i).getProgress();
            totalKRProgress += (krProgress >= 70) ? 100 : krProgress;
        }
        short averageProgress = (short) (totalKRProgress / objective.getKeyResultList().size());
        System.out.println(averageProgress);
        return averageProgress;
    }

}
