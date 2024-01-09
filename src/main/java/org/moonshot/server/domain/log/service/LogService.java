package org.moonshot.server.domain.log.service;

import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultCreateRequestDto;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultCreateRequestInfoDto;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultModifyRequestDto;
import org.moonshot.server.domain.keyresult.exception.KeyResultNotFoundException;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.keyresult.repository.KeyResultRepository;
import org.moonshot.server.domain.log.dto.request.LogCreateRequestDto;
import org.moonshot.server.domain.log.model.Log;
import org.moonshot.server.domain.log.model.LogState;
import org.moonshot.server.domain.log.repository.LogRepository;
import org.moonshot.server.domain.user.exception.UserNotFoundException;
import org.moonshot.server.domain.user.model.User;
import org.moonshot.server.domain.user.repository.UserRepository;
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
        User user =  userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        KeyResult keyResult = keyResultRepository.findById(request.keyResultId())
                .orElseThrow(KeyResultNotFoundException::new);
        List<Log> prevLog = logRepository.findLatestLogByKeyResultId(request.keyResultId());
        long prevNum = -1;
        if (!prevLog.isEmpty()) {
            prevNum = prevLog.get(0).getCurrNum();
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
                .currNum(request.target()) // 바꾸는 값
                .prevNum(keyResult.getTarget()) // 이전 값
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

}
