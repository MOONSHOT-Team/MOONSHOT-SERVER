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

    @Test
    @DisplayName("KeyResult의 진척 상황의 값이 이전 진척 상황 값과 동일하여 예외가 발생합니다.")
    void KeyResult의_진척상황_값이_이전_진척상황_값과_동일하여_예외가_발생합니다() {
        //given
        Objective testObjective = mock(Objective.class);
        KeyResult testKeyResult = mock(KeyResult.class);
        Log testPrevLog = mock(Log.class);
        LogCreateRequestDto request = new LogCreateRequestDto(1L, 1000L, "new log content");

        given(keyResultRepository.findKeyResultAndObjective(request.keyResultId())).willReturn(Optional.of(testKeyResult));
        given(testKeyResult.getObjective()).willReturn(testObjective);
        given(testObjective.getUser()).willReturn(fakeUser);

        given(logRepository.findLatestLogByKeyResultId(eq(LogState.RECORD), anyLong())).willReturn(Optional.of(testPrevLog));
        given(testPrevLog.getCurrNum()).willReturn(1000L);

        //when, then
        assertThatThrownBy(() -> logService.createRecordLog(fakeUser.getId(), request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Log 입력값은 이전 값과 동일할 수 없습니다.");
    }

    @Test
    @DisplayName("KeyResult가 수정되면 체크인 로그를 추가합니다.")
    void KeyResult가_수정되면_체크인_로그를_추가합니다() {
        //given
        KeyResult testKeyResult = mock(KeyResult.class);
        KeyResultModifyRequestDto request = new KeyResultModifyRequestDto(
                1L, null, null, null, 100000L, null, "new check-in");
        //when
        logService.createUpdateLog(request, testKeyResult);

        //then
        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    @DisplayName("Objective와 함께 KeyResult가 생성되면 체크인 로그를 추가합니다.")
    void Objective와_함께_KeyResult가_생성되면_체크인_로그를_추가합니다() {
        //given
        KeyResult testKeyResult = mock(KeyResult.class);
        Long testKeyResultId = 1L;
        KeyResultCreateRequestInfoDto request = new KeyResultCreateRequestInfoDto("test KR", LocalDate.now(), LocalDate.now(), 0, 2000L, "건", null);

        given(keyResultRepository.findById(testKeyResultId)).willReturn(Optional.of(testKeyResult));

        //when
        logService.createKRLog(request, testKeyResultId);

        //then
        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    @DisplayName("KeyResult가 추가적으로 생성되면 체크인 로그를 추가합니다.")
    void KeyResult가_추가적으로_생성되면_체크인_로그를_추가합니다() {
        //given
        KeyResult testKeyResult = mock(KeyResult.class);
        Long testKeyResultId = 1L;
        KeyResultCreateRequestDto request = new KeyResultCreateRequestDto(1L, "test KR", LocalDate.now(), LocalDate.now(), 0, 2000L, "건");

        given(keyResultRepository.findById(testKeyResultId)).willReturn(Optional.of(testKeyResult));

        //when
        logService.createKRLog(request, testKeyResultId);

        //then
        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    @DisplayName("진척 상황값이 입력되면 KR 달성률을 계산합니다.")
    void 진척상황_값이_입력되면_KR_달성률을_계산합니다() {
        //given
        KeyResult testKeyResult = mock(KeyResult.class);
        Log testLog = mock(Log.class);

        given(testKeyResult.getTarget()).willReturn(1000L);
        given(testLog.getCurrNum()).willReturn(100L);

        //when, then
        assertThat(logService.calculateKRProgressBar(testLog, testKeyResult.getTarget())).isEqualTo((short)10);
    }

}