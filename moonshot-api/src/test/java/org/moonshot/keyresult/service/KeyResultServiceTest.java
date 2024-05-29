package org.moonshot.keyresult.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.moonshot.common.model.Period;
import org.moonshot.exception.BadRequestException;
import org.moonshot.keyresult.dto.request.KeyResultCreateRequestDto;
import org.moonshot.keyresult.dto.request.KeyResultCreateRequestInfoDto;
import org.moonshot.keyresult.dto.request.KeyResultModifyRequestDto;
import org.moonshot.keyresult.dto.response.KRDetailResponseDto;
import org.moonshot.keyresult.model.KRState;
import org.moonshot.keyresult.model.KeyResult;
import org.moonshot.keyresult.repository.KeyResultRepository;
import org.moonshot.log.dto.response.AchieveResponseDto;
import org.moonshot.log.model.Log;
import org.moonshot.log.model.LogState;
import org.moonshot.log.repository.LogRepository;
import org.moonshot.log.service.LogService;
import org.moonshot.objective.model.Category;
import org.moonshot.objective.model.Objective;
import org.moonshot.objective.repository.ObjectiveRepository;
import org.moonshot.task.dto.request.TaskCreateRequestDto;
import org.moonshot.task.repository.TaskRepository;
import org.moonshot.task.service.TaskService;
import org.moonshot.user.model.User;

@ExtendWith(MockitoExtension.class)
class KeyResultServiceTest {

    @Mock
    private ObjectiveRepository objectiveRepository;
    @Mock
    private KeyResultRepository keyResultRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private LogService logService;
    @Mock
    private LogRepository logRepository;
    @InjectMocks
    private KeyResultService keyResultService;

    private static User fakeUser;

    @BeforeAll
    static void setUp() {
        Long fakeUserId = 1L;
        String fakeUserNickname = "tester";
        fakeUser = User.buildWithId().id(fakeUserId).nickname(fakeUserNickname).build();
    }

    @Test
    @DisplayName("Objective와 함께 생성하는 KeyResult를 생성합니다")
    void Objective와_함께_생성하는_KeyResult를_생성합니다() {
        //given
        Objective testObjective = Objective.builder()
                .title("testObjective")
                .category(Category.ECONOMY)
                .content("testObjective Content")
                .period(Period.of(LocalDate.now(), LocalDate.from(LocalDate.now().plusDays(30))))
                .user(fakeUser)
                .build();
        KeyResult testKeyResult = mock(KeyResult.class);
        TaskCreateRequestDto firstTaskDto = new TaskCreateRequestDto("test Task 0", 0);
        TaskCreateRequestDto secondTaskDto = new TaskCreateRequestDto("test Task 1", 1);
        TaskCreateRequestDto thirdTaskDto = new TaskCreateRequestDto("test Task 2", 2);
        List<TaskCreateRequestDto> taskList = List.of(firstTaskDto, secondTaskDto, thirdTaskDto);
        KeyResultCreateRequestInfoDto firstKeyResultDto = new KeyResultCreateRequestInfoDto(
                "test KR", LocalDate.now(), LocalDate.from(LocalDate.now().plusDays(30)),
                0, 500000L, "건", taskList
        );
        KeyResultCreateRequestInfoDto secondKeyResultDto = new KeyResultCreateRequestInfoDto(
                "test KR", LocalDate.now(), LocalDate.from(LocalDate.now().plusDays(30)),
                1, 500000L, "건", taskList
        );
        List<KeyResultCreateRequestInfoDto> testRequests = new java.util.ArrayList<>(
                List.of(firstKeyResultDto, secondKeyResultDto));
        testRequests.add(null);
        given(keyResultRepository.save(any(KeyResult.class))).willReturn(testKeyResult);
        given(testKeyResult.getId()).willReturn(1L);

        //when
        keyResultService.createInitKRWithObjective(testObjective, testRequests);

        //then
        verify(keyResultRepository, times(2)).save(any(KeyResult.class));
        verify(logService, times(2)).createKRLog(any(KeyResultCreateRequestInfoDto.class), any(Long.class));
        verify(taskService, times(6)).saveTask(any(KeyResult.class), any(TaskCreateRequestDto.class));
    }

    @Test
    @DisplayName("단일 KeyResult를 생성합니다")
    void 단일_KeyResult를_생성합니다() {
        // given
        Objective testObjective = mock(Objective.class);
        User testUser = mock(User.class);
        KeyResult testKeyResult = mock(KeyResult.class);
        KeyResultCreateRequestDto request = new KeyResultCreateRequestDto(
                1L, "test KR", LocalDate.now(),
                LocalDate.from(LocalDate.now().plusDays(30)),
                0, 500000L, "건");

        given(objectiveRepository.findObjectiveAndUserById(1L)).willReturn(Optional.of(testObjective));
        given(keyResultRepository.findAllByObjective(testObjective)).willReturn(List.of(testKeyResult));
        given(testObjective.getId()).willReturn(1L);
        given(testObjective.getUser()).willReturn(testUser);
        given(testUser.getId()).willReturn(1L);
        given(keyResultRepository.save(any(KeyResult.class))).willReturn(testKeyResult);
        given(testKeyResult.getId()).willReturn(2L);

        // when
        keyResultService.createKeyResult(request, fakeUser.getId());

        // then
        verify(keyResultRepository, times(1)).bulkUpdateIdxIncrease(any(Integer.class), any(Integer.class), eq(1L), eq(-1L));
        verify(logService, times(1)).createKRLog(request, 2L);
    }

    @Test
    @DisplayName("KeyResult 생성 시 최대 보유 갯수를 넘어 예외가 발생합니다")
    void KeyResult_생성_시_최대_보유_갯수를_넘어_예외가_발생합니다() {
        // given
        Objective testObjective = mock(Objective.class);
        User testUser = mock(User.class);
        List krList = mock(List.class);
        KeyResultCreateRequestDto request = new KeyResultCreateRequestDto(
                1L, "test KR", LocalDate.now(),
                LocalDate.from(LocalDate.now().plusDays(30)),
                0, 500000L, "건");

        given(objectiveRepository.findObjectiveAndUserById(1L)).willReturn(Optional.of(testObjective));
        given(keyResultRepository.findAllByObjective(testObjective)).willReturn(krList);
        given(krList.size()).willReturn(3);
        given(testObjective.getUser()).willReturn(testUser);
        given(testUser.getId()).willReturn(1L);

        // when, then
        assertThatThrownBy(() -> keyResultService.createKeyResult(request, fakeUser.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("허용된 Key Result 개수를 초과하였습니다");
    }

    @Test
    @DisplayName("KeyResult 생성 시 인덱스가 0 ~ ListSize(최대 개수의 범위)를 벗어나 요청된 경우 예외가 발생합니다")
    void KeyResult_생성_시_인덱스가_0_ListSize를_벗어나_요청된_경우_예외가_발생합니다() {
        // given
        Objective testObjective = mock(Objective.class);
        User testUser = mock(User.class);
        List krList = mock(List.class);
        KeyResultCreateRequestDto request = new KeyResultCreateRequestDto(
                1L, "test KR", LocalDate.now(),
                LocalDate.from(LocalDate.now().plusDays(30)),
                2, 500000L, "건");

        given(objectiveRepository.findObjectiveAndUserById(1L)).willReturn(Optional.of(testObjective));
        given(keyResultRepository.findAllByObjective(testObjective)).willReturn(krList);
        given(krList.size()).willReturn(1);
        given(testObjective.getUser()).willReturn(testUser);
        given(testUser.getId()).willReturn(1L);

        // when, then
        assertThatThrownBy(() -> keyResultService.createKeyResult(request, fakeUser.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("정상적이지 않은 KeyResult 위치입니다.");
    }

    @Test
    @DisplayName("KeyResult의 데이터를 조회합니다")
    void KeyResult의_데이터를_조회합니다() {
        // given
        Long testKeyResultId = 99L;
        Objective testObjective = Objective.builder()
                .title("testObjective")
                .user(fakeUser).build();
        KeyResult testKeyResult = KeyResult.builder()
                .title("testKeyResult")
                .objective(testObjective)
                .target(1L)
                .metric("건")
                .idx(0)
                .state(KRState.PROGRESS)
                .period(Period.of(LocalDate.now(), LocalDate.from(LocalDate.now().plusDays(30))))
                .build();
        Log testLog = Log.builder().keyResult(testKeyResult).state(LogState.RECORD).currNum(10000L).build();
        List<Log> logList = List.of(testLog);
        given(keyResultRepository.findKeyResultAndObjective(testKeyResultId)).willReturn(Optional.of(testKeyResult));
        given(logService.getLogList(testKeyResult)).willReturn(logList);
        given(logService.calculateKRProgressBar(testLog, testKeyResult.getTarget())).willReturn((short) 100);

        // when
        KRDetailResponseDto krDetails = keyResultService.getKRDetails(fakeUser.getId(), testKeyResultId);

        // then
        assertThat(krDetails.target()).isEqualTo(1L);
        assertThat(krDetails.metric()).isEqualTo("건");
        assertThat(krDetails.progressBar()).isEqualTo((short) 100);
        assertThat(krDetails.krState()).isEqualTo(KRState.PROGRESS.getValue());
        assertThat(krDetails.title()).isEqualTo("testKeyResult : ");
    }

    @Test
    @DisplayName("KeyResult의 제목 및 날짜 데이터를 수정합니다")
    void KeyResult의_제목_및_날짜_데이터를_수정합니다() {
        // given
        Long testKeyResultId = 99L;
        KeyResult testKeyResult = mock(KeyResult.class);
        Objective testObjective = mock(Objective.class);
        KeyResultModifyRequestDto request = new KeyResultModifyRequestDto(
                testKeyResultId, "modified KeyResult Title", LocalDate.of(2024, 4, 1),
                LocalDate.of(2024, 5, 1), null, null, null);
        given(keyResultRepository.findKeyResultAndObjective(testKeyResultId)).willReturn(Optional.of(testKeyResult));
        given(testKeyResult.getObjective()).willReturn(testObjective);
        given(testKeyResult.getPeriod()).willReturn(Period.of(LocalDate.of(2023, 4, 1), LocalDate.of(2023, 5, 1)));
        given(testObjective.getPeriod()).willReturn(Period.of(LocalDate.of(2023, 4, 1), LocalDate.of(2025, 5, 1)));
        given(testObjective.getUser()).willReturn(fakeUser);

        // when
        keyResultService.modifyKeyResult(request, fakeUser.getId());

        // then
        verify(testKeyResult, times(1)).modifyTitle(request.krTitle());
        verify(testKeyResult, times(1)).modifyPeriod(any(Period.class));
    }

    @Test
    @DisplayName("KeyResult의 상태 데이터를 수정합니다")
    void KeyResult의_상태_데이터를_수정합니다() {
        // given
        Long testKeyResultId = 99L;
        KeyResult testKeyResult = mock(KeyResult.class);
        Objective testObjective = mock(Objective.class);
        KeyResultModifyRequestDto request = new KeyResultModifyRequestDto(
                testKeyResultId, null, null, null, null, KRState.DONE, null);
        given(keyResultRepository.findKeyResultAndObjective(testKeyResultId)).willReturn(Optional.of(testKeyResult));
        given(testKeyResult.getObjective()).willReturn(testObjective);
        given(testObjective.getUser()).willReturn(fakeUser);

        // when
        Optional<AchieveResponseDto> response = keyResultService.modifyKeyResult(request, fakeUser.getId());

        // then
        verify(testKeyResult, times(1)).modifyState(KRState.DONE);
        assertThat(response.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("KeyResult 수정 시 목표치와 체크인 내용이 둘다 없을 경우 예외가 발생합니다")
    void KeyResult_수정_시_목표치와_체크인_내용이_둘다_없을_경우_예외가_발생합니다() {
        // given
        Long testKeyResultId = 99L;
        KeyResult testKeyResult = mock(KeyResult.class);
        Objective testObjective = mock(Objective.class);
        KeyResultModifyRequestDto request = new KeyResultModifyRequestDto(
                testKeyResultId, null, null, null, null, null, null);
        given(keyResultRepository.findKeyResultAndObjective(testKeyResultId)).willReturn(Optional.of(testKeyResult));
        given(testKeyResult.getObjective()).willReturn(testObjective);
        given(testObjective.getUser()).willReturn(fakeUser);

        // when, then
        assertThatThrownBy(() -> keyResultService.modifyKeyResult(request, fakeUser.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("KR 수정시 목표값과 체크인 로그는 필수 입력값입니다.");
    }

    @Test
    @DisplayName("KeyResult의 목표치를 수정하고 체크인 로그를 추가합니다")
    void KeyResult의_목표치를_수정하고_체크인_로그를_추가합니다() {
        // given
        Long testKeyResultId = 99L;
        KeyResult testKeyResult = mock(KeyResult.class);
        Objective testObjective = mock(Objective.class);
        Log testLog = mock(Log.class);
        Log prevTestLog = mock(Log.class);
        KeyResultModifyRequestDto request = new KeyResultModifyRequestDto(
                testKeyResultId, null, null, null, 100000L, null, "new check-in");
        given(keyResultRepository.findKeyResultAndObjective(testKeyResultId)).willReturn(Optional.of(testKeyResult));
        given(testKeyResult.getObjective()).willReturn(testObjective);
        given(testKeyResult.getTarget()).willReturn(1L);
        doNothing().when(testKeyResult).modifyTarget(request.krTarget());

        given(testObjective.getUser()).willReturn(fakeUser);
        given(testLog.getKeyResult()).willReturn(testKeyResult);
        given(logService.createUpdateLog(request, testKeyResult)).willReturn(testLog);
        given(logRepository.findLatestLogByKeyResultId(LogState.RECORD, testKeyResultId)).willReturn(Optional.of(prevTestLog));

        /* isKeyResultAchieved */
        given(testObjective.getProgress()).willReturn((short) 1);

        // when
        Optional<AchieveResponseDto> response = keyResultService.modifyKeyResult(request, fakeUser.getId());

        // then
        verify(testKeyResult, times(1)).modifyProgress(any(Short.class));
        verify(testObjective, times(1)).modifyProgress(any(Short.class));
        assertThat(response.isEmpty()).isEqualTo(true);
    }

}