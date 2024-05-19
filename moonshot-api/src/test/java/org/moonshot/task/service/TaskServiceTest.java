package org.moonshot.task.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.moonshot.exception.BadRequestException;
import org.moonshot.keyresult.model.KeyResult;
import org.moonshot.keyresult.repository.KeyResultRepository;
import org.moonshot.objective.model.Objective;
import org.moonshot.task.dto.request.TaskSingleCreateRequestDto;
import org.moonshot.task.repository.TaskRepository;
import org.moonshot.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private KeyResultRepository keyResultRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private static User fakeUser;

    @BeforeAll
    static void setUp() {
        Long fakeUserId = 1L;
        String fakeUserNickname = "tester";
        fakeUser = User.buildWithId().id(fakeUserId).nickname(fakeUserNickname).build();
    }

    @Test
    @DisplayName("단일 Task를 생성합니다")
    void 단일_Task를_생성합니다() {
        // given
        Objective testObjective = mock(Objective.class);
        KeyResult testKeyResult = mock(KeyResult.class);
        TaskSingleCreateRequestDto request = new TaskSingleCreateRequestDto(
                1L, "test task", 0);

        given(keyResultRepository.findKeyResultAndObjective(request.keyResultId())).willReturn(Optional.of(testKeyResult));
        given(testKeyResult.getObjective()).willReturn(testObjective);
        given(testObjective.getUser()).willReturn(fakeUser);
        given(testKeyResult.getId()).willReturn(1L);

        // when
        taskService.createTask(request, fakeUser.getId());

        // then
        verify(taskRepository, times(1)).bulkUpdateTaskIdxIncrease(any(Integer.class), any(Integer.class), eq(1L), eq(-1L));
    }

}
