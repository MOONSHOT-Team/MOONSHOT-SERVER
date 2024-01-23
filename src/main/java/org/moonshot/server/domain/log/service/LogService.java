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
@Transactional
@RequiredArgsConstructor
public class LogService {

    private final KeyResultRepository keyResultRepository;
    private final LogRepository logRepository;

    public Optional<AchieveResponseDto> createRecordLog(final Long userId, final LogCreateRequestDto request) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(request.keyResultId())
                .orElseThrow(KeyResultNotFoundException::new);
        if (!keyResult.getObjective().getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }
        Optional<Log> prevLog = logRepository.findLatestLogByKeyResultId(LogState.RECORD, request.keyResultId());
        long prevNum = -1;
        if (prevLog.isPresent()) {
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

    public Log createUpdateLog(final KeyResultModifyRequestDto request, final Long keyResultId) {
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

    public void createKRLog(final Object request, final Long keyResultId) {
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
        if (log.getState() == LogState.CREATE) {
            return keyResult.getTitle() + " : " + keyResult.getTarget() + keyResult.getMetric();
        } else {
            return (prevNum == -1 ? "0" : NumberFormat.getNumberInstance().format(prevNum)) + keyResult.getMetric()
                    + " → " + NumberFormat.getNumberInstance().format(currNum) + keyResult.getMetric();
        }
    }

    public short calculateKRProgressBar(final Log log, final KeyResult keyResult) {
        return (log != null) ? (short) (Math.round(log.getCurrNum() / (double) keyResult.getTarget() * 100)) : 0;
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
