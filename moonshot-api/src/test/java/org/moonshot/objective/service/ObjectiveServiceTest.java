package org.moonshot.objective.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
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
import org.moonshot.keyresult.service.KeyResultService;
import org.moonshot.objective.dto.request.ModifyObjectiveRequestDto;
import org.moonshot.objective.dto.request.OKRCreateRequestDto;
import org.moonshot.objective.dto.response.DashboardResponseDto;
import org.moonshot.objective.dto.response.HistoryResponseDto;
import org.moonshot.objective.dto.response.ObjectiveGroupByYearDto;
import org.moonshot.objective.model.Category;
import org.moonshot.objective.model.Criteria;
import org.moonshot.objective.model.Objective;
import org.moonshot.objective.repository.ObjectiveRepository;
import org.moonshot.user.model.User;
import org.moonshot.user.repository.UserRepository;
import org.moonshot.user.service.UserService;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
class ObjectiveServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private KeyResultService keyResultService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ObjectiveRepository objectiveRepository;
    @InjectMocks
    private ObjectiveService objectiveService;

    private static User fakeUser;

    @BeforeAll
    static void setUp() {
        Long fakeUserId = 1L;
        String fakeUserNickname = "tester";
        fakeUser = User.buildWithId().id(fakeUserId).nickname(fakeUserNickname).build();
    }

    @Test
    @DisplayName("Objective를 생성합니다")
    void Objective를_생성합니다() {
        // given
        OKRCreateRequestDto testDto = OKRCreateRequestDto.builder()
                .objTitle("test")
                .objCategory(Category.ECONOMY)
                .objContent("test")
                .objStartAt(LocalDate.now())
                .objExpireAt(LocalDate.from(LocalDate.now().plusDays(30)))
                .krList(new ArrayList<>())
                .build();
        Objective objective = Objective.builder()
                .title(testDto.objTitle())
                .category(testDto.objCategory())
                .content(testDto.objContent())
                .period(Period.of(testDto.objStartAt(), testDto.objExpireAt()))
                .user(fakeUser).build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(fakeUser));
        when(objectiveRepository.findAllByUserId(anyLong())).thenReturn(new ArrayList<>());
        when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

        // when
        objectiveService.createObjective(1L, testDto);
        // then
        verify(objectiveRepository, times(1)).save(any(Objective.class));
    }

    @Test
    @Transactional
    @DisplayName("Objective를 수정합니다")
    void Objective를_수정합니다() {
        // given
        Objective testObjective = Objective.builder()
                .title("testObjective")
                .category(Category.ECONOMY)
                .content("test")
                .period(Period.of(LocalDate.now(), LocalDate.from(LocalDate.now().plusDays(30))))
                .user(fakeUser).build();
        LocalDate changedExpireAt = LocalDate.from(LocalDate.now().plusDays(25));
        ModifyObjectiveRequestDto request = ModifyObjectiveRequestDto.builder()
                .isClosed(true)
                .expireAt(changedExpireAt).build();

        when(objectiveRepository.findObjectiveAndUserById(anyLong())).thenReturn(Optional.ofNullable(testObjective));

        // when
        objectiveService.modifyObjective(fakeUser.getId(), request);

        // then
        assertThat(testObjective.isClosed()).isEqualTo(true);
    }

    @Test
    @DisplayName("비어있는 Dashboard Objective를 조회합니다")
    void 비어있는_Dashboard_Objective를_조회합니다() {
        // given
        Long fakeObjectiveId = 1L;
        when(objectiveRepository.findAllByUserId(fakeUser.getId())).thenReturn(new ArrayList<>());
        when(userRepository.findById(fakeUser.getId())).thenReturn(Optional.ofNullable(fakeUser));

        // when
        DashboardResponseDto response = objectiveService.getObjectiveInDashboard(fakeUser.getId(), fakeObjectiveId);

        // then
        assertThat(response.nickname()).isEqualTo(fakeUser.getNickname());
    }

    @Test
    @DisplayName("여러개의 Dashboard Objective를 조회합니다")
    void 여러개의_Dashboard_Objective를_조회합니다() {
        // given
        Long fakeObjectiveId = 1L;
        Objective objective1 = Objective.builder()
                .user(fakeUser)
                .title("Objective1")
                .content("Objective1")
                .period(Period.of(LocalDate.now(), LocalDate.from(LocalDate.now().plusDays(30))))
                .category(Category.ECONOMY)
                .build();
        Objective objective2 = Objective.builder()
                .user(fakeUser)
                .title("Objective2")
                .period(Period.of(LocalDate.now(), LocalDate.from(LocalDate.now().plusDays(30))))
                .category(Category.ECONOMY)
                .content("Objective2")
                .build();
        when(objectiveRepository.findAllByUserId(fakeUser.getId())).thenReturn(List.of(objective1, objective2));
        when(objectiveRepository.findByIdWithKeyResultsAndTasks(fakeObjectiveId)).thenReturn(Optional.ofNullable(objective1));

        // when
        DashboardResponseDto response = objectiveService.getObjectiveInDashboard(fakeUser.getId(), fakeObjectiveId);

        // then
        assertThat(response.objList().size()).isEqualTo(2);
        assertThat(response.objList().get(1).title()).isEqualTo("Objective2");
    }

    @Test
    @DisplayName("Objective의 히스토리를 조회합니다")
    void Objective의_히스토리를_조회합니다() {
        //given
        Objective objective1 = Objective.builder()
                .user(fakeUser)
                .title("Objective 1")
                .content("Objective 1")
                .category(Category.ECONOMY)
                .period(Period.of(LocalDate.of(2023, 1, 1), LocalDate.now()))
                .build();
        Objective objective2 = Objective.builder()
                .user(fakeUser)
                .title("Objective 2")
                .content("Objective 2")
                .category(Category.GROWTH)
                .period(Period.of(LocalDate.of(2023, 2, 1), LocalDate.now()))
                .build();
        Objective objective3 = Objective.builder()
                .user(fakeUser)
                .title("Objective 3")
                .content("Objective 3")
                .category(Category.GROWTH)
                .period(Period.of(LocalDate.of(2023, 1, 15), LocalDate.now()))
                .build();
        List<Objective> testObjectives = List.of(objective2, objective3);

        when(objectiveRepository.findObjectives(fakeUser.getId(), 2023, Category.GROWTH, Criteria.LATEST)).thenReturn(testObjectives);

        // when
        HistoryResponseDto response = objectiveService.getObjectiveHistory(fakeUser.getId(), 2023, Category.GROWTH, Criteria.LATEST);

        // then
        assertThat(response).isNotNull();
        assertThat(response.groups().size()).isEqualTo(1);

        verify(objectiveRepository).findObjectives(fakeUser.getId(), 2023, Category.GROWTH, Criteria.LATEST);

        ObjectiveGroupByYearDto dto = response.groups().get(0);
        assertThat(dto.year()).isEqualTo(2023);
        assertThat(dto.objList().size()).isEqualTo(2);
        assertThat(dto.objList().get(0).title()).isEqualTo("Objective 2");
    }

}