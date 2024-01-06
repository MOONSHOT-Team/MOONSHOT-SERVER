package org.moonshot.server.domain.keyresult.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultCreateRequestDto;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultCreateRequestInfoDto;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultModifyRequestDto;
import org.moonshot.server.domain.keyresult.exception.KeyResultInvalidPositionException;
import org.moonshot.server.domain.keyresult.exception.KeyResultNotFoundException;
import org.moonshot.server.domain.keyresult.exception.KeyResultNumberExceededException;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.keyresult.repository.KeyResultRepository;
import org.moonshot.server.domain.objective.exception.ObjectiveNotFoundException;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.domain.objective.repository.ObjectiveRepository;
import org.moonshot.server.domain.task.model.Task;
import org.moonshot.server.domain.task.repository.TaskRepository;
import org.moonshot.server.global.common.model.Period;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KeyResultService {

    private static final int ACTIVE_KEY_RESULT_NUMBER = 3;

    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final TaskRepository taskRepository;

    //TODO
    // 여기 모든 로직에 User 관련 기능이 추가된 이후
    // KeyResult의 소유자인지 확인하는 절차를 추가해야 함.

    @Transactional
    public void createInitKRWithObjective(Objective objective, List<KeyResultCreateRequestInfoDto> requests) {
        for (KeyResultCreateRequestInfoDto dto : requests) {
            KeyResult keyResult = keyResultRepository.save(KeyResult.builder()
                    .title(dto.title())
                    .period(Period.of(dto.startAt(), dto.expireAt()))
                    .idx(dto.idx())
                    .target(dto.target())
                    .metric(dto.metric())
                    .descriptionBefore(dto.descriptionBefore())
                    .descriptionAfter(dto.descriptionAfter())
                    .objective(objective)
                    .build());
            if (dto.taskList() != null) {
                taskRepository.saveAll(dto.taskList().stream().map((task) -> Task.builder()
                        .title(task.title())
                        .idx(task.idx())
                        .keyResult(keyResult)
                        .build()).toList());
            }
        }
    }

    @Transactional
    public void createKeyResult(KeyResultCreateRequestDto request) {
        Objective objective = objectiveRepository.findById(request.objectiveId())
                .orElseThrow(ObjectiveNotFoundException::new);
        List<KeyResult> krList = keyResultRepository.findAllByObjective(objective);

        if (krList.size() >= ACTIVE_KEY_RESULT_NUMBER) {
            throw new KeyResultNumberExceededException();
        }
        if (request.idx() > krList.size()) {
            throw new KeyResultInvalidPositionException();
        }

        for (short i = request.idx(); i < krList.size(); i++) {
            krList.get(i).incrementIdx();
        }
        keyResultRepository.save(KeyResult.builder()
                .objective(objective)
                .title(request.title())
                .period(Period.of(request.startAt(), request.expireAt()))
                .idx(request.idx())
                .target(request.target())
                .metric(request.metric())
                .descriptionBefore(request.descriptionBefore())
                .descriptionAfter(request.descriptionAfter()).build());
    }

    @Transactional
    public void deleteKeyResult(List<Long> keyResultIds) {
        cascadeDelete(keyResultRepository.findByIdIn(keyResultIds));
    }

    @Transactional
    public void deleteKeyResult(Objective objective) {
        cascadeDelete(keyResultRepository.findAllByObjective(objective));
    }

    private void cascadeDelete(List<KeyResult> krList) {
        krList.forEach((kr) -> {
            taskRepository.deleteAllInBatch(taskRepository.findAllByKeyResult(kr));
        });
        keyResultRepository.deleteAllInBatch(krList);
    }

    @Transactional
    public void modifyKeyResult(KeyResultModifyRequestDto request) {
        KeyResult keyResult = keyResultRepository.findById(request.keyResultId())
                .orElseThrow(KeyResultNotFoundException::new);

        if (request.title() != null) {
            keyResult.modifyTitle(request.title());
        }
        if (request.startAt() != null) {
            if (request.expireAt() != null) {
                keyResult.modifyPeriod(Period.of(request.startAt(), request.expireAt()));
            }
            keyResult.modifyPeriod(Period.of(request.startAt(), keyResult.getPeriod().getExpireAt()));
        } else {
            if (request.expireAt() != null) {
                keyResult.modifyPeriod(Period.of(keyResult.getPeriod().getStartAt(), request.expireAt()));
            }
        }
        if (request.target() != null) {
            keyResult.modifyTarget(request.target());
        }
    }

}
