package org.moonshot.server.domain.log.service;

import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultCreateRequestDto;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultCreateRequestInfoDto;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultModifyRequestDto;
import org.moonshot.server.domain.keyresult.exception.KeyResultNotFoundException;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.keyresult.repository.KeyResultRepository;
import org.moonshot.server.domain.log.dto.request.LogCreateRequestDto;
import org.moonshot.server.domain.log.dto.response.LogResponseDto;
import org.moonshot.server.domain.log.model.Log;
import org.moonshot.server.domain.log.model.LogState;
import org.moonshot.server.domain.log.repository.LogRepository;
import org.moonshot.server.domain.user.exception.UserNotFoundException;
import org.moonshot.server.domain.user.model.User;
import org.moonshot.server.domain.user.repository.UserRepository;
import org.moonshot.server.global.auth.exception.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LogService {

    private final UserRepository userRepository;
    private final KeyResultRepository keyResultRepository;
    private final LogRepository logRepository;

    @Transactional
    public void createRecordLog(Long userId, LogCreateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        KeyResult keyResult = keyResultRepository.findById(request.keyResultId())
                .orElseThrow(KeyResultNotFoundException::new);
        if (!keyResult.getObjective().getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }
        Optional<Log> prevLog = logRepository.findLatestLogByKeyResultId(LogState.RECORD, request.keyResultId());
        long prevNum = -1;
        if (!prevLog.isEmpty()) {
            prevNum = prevLog.get().getCurrNum();
        }
        logRepository.save(Log.builder()
                .date(LocalDateTime.now())
                .state(LogState.RECORD)
                .currNum(request.logNum())
                .prevNum(prevNum)
                .content(request.logContent())
                .keyResult(keyResult)
                .build());
    }

    @Transactional
    public void createUpdateLog(KeyResultModifyRequestDto request, Long keyResultId) {
        KeyResult keyResult = keyResultRepository.findById(keyResultId)
                .orElseThrow(KeyResultNotFoundException::new);

        logRepository.save(Log.builder()
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

    public List<LogResponseDto> getLogList(KeyResult keyResult) {
        return logRepository.findAllByKeyResultOrderByIdDesc(keyResult)
                .stream()
                .map(log -> LogResponseDto.of(log.getState().getValue(),
                        log.getDate(),
                        setTitle(log),
                        log.getContent()))
                .toList();
    }

    public String setTitle(Log log) {
        if (log.getState() == LogState.CREATE) {
            Optional<Log> createLog = logRepository.findOldestLogByKeyResultId(log.getKeyResult().getId());
            return log.getKeyResult().getDescriptionBefore() + " " + createLog.get().getKeyResult().getTarget() + log.getKeyResult().getMetric() + " " + log.getKeyResult().getDescriptionAfter();
        } else if(log.getState() == LogState.UPDATE) {
            Optional<Log> updateLog = logRepository.findLatestLogByKeyResultId(LogState.UPDATE, log.getKeyResult().getId());
            return updateLog.get().getPrevNum() + log.getKeyResult().getMetric() + " → " + log.getCurrNum() + log.getKeyResult().getMetric();
        } else {
            Optional<Log> recordLog = logRepository.findLatestLogByKeyResultId(LogState.RECORD, log.getKeyResult().getId());
            return (recordLog.get().getPrevNum() == -1 ? "0" : recordLog.get().getPrevNum()) + log.getKeyResult().getMetric() + " → " + log.getCurrNum() + log.getKeyResult().getMetric();
        }
    }

}
