package org.moonshot.log.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.moonshot.exception.BadRequestException;
import org.moonshot.keyresult.dto.request.KeyResultCreateRequestDto;
import org.moonshot.keyresult.dto.request.KeyResultCreateRequestInfoDto;
import org.moonshot.keyresult.dto.request.KeyResultModifyRequestDto;
import org.moonshot.keyresult.model.KeyResult;
import org.moonshot.keyresult.repository.KeyResultRepository;
import org.moonshot.log.dto.request.LogCreateRequestDto;
import org.moonshot.log.dto.response.AchieveResponseDto;
import org.moonshot.log.model.Log;
import org.moonshot.log.model.LogState;
import org.moonshot.log.repository.LogRepository;
import org.moonshot.objective.model.Objective;
import org.moonshot.user.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {

    @Mock
    private KeyResultRepository keyResultRepository;
    @InjectMocks
    private LogService logService;
    @Mock
    private LogRepository logRepository;

    private static User fakeUser;


    @BeforeAll
    static void setUp() {
        Long fakeUserId = 1L;
        String fakeUserNickname = "tester";
        fakeUser = User.buildWithId().id(fakeUserId).nickname(fakeUserNickname).build();
    }

    @Test
    @DisplayName("KeyResult의 진척 상황을 기록하고 체크인 로그를 추가합니다.")
    void KeyResult의_진척상황을_기록하고_체크인_로그를_추가합니다() {
        // given
        Objective testObjective = mock(Objective.class);
        KeyResult testKeyResult = mock(KeyResult.class);
        Log testLog = mock(Log.class);
        Log testPrevLog = mock(Log.class);
        LogCreateRequestDto request = new LogCreateRequestDto(1L, 1000L, "new log content");

        given(keyResultRepository.findKeyResultAndObjective(request.keyResultId())).willReturn(Optional.of(testKeyResult));
        given(testKeyResult.getObjective()).willReturn(testObjective);
        given(testObjective.getUser()).willReturn(fakeUser);

        given(logRepository.findLatestLogByKeyResultId(eq(LogState.RECORD), anyLong())).willReturn(Optional.of(testPrevLog));
        given(logRepository.save(any(Log.class))).willReturn(testLog);
        given(testObjective.getKeyResultList()).willReturn(List.of(testKeyResult));

        given(testObjective.getProgress()).willReturn((short)1);

        //when
        Optional<AchieveResponseDto> response = logService.createRecordLog(fakeUser.getId(), request);

        //then
        verify(testKeyResult, times(1)).modifyProgress(anyShort());
        verify(testObjective, times(1)).modifyProgress(anyShort());
        assertThat(response.isEmpty()).isEqualTo(true);
    }

}