package org.moonshot.server.domain.keyresult.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.keyresult.model.KRState;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.keyresult.repository.KeyResultRepository;
import org.moonshot.server.domain.objective.dto.request.KRCreateRequestDto;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.domain.task.model.Task;
import org.moonshot.server.domain.task.repository.TaskRepository;
import org.moonshot.server.global.common.model.Period;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KeyResultService {

    private final KeyResultRepository keyResultRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public void createInitWithObjective(Objective objective, List<KeyResultCreateRequestInfoDto> requests) {
        for (KeyResultCreateRequestInfoDto dto : requests) {
            KeyResult keyResult = keyResultRepository.save(KeyResult.builder()
                    .title(dto.title())
                    .period(Period.of(dto.startAt(), dto.expireAt()))
                    .order(dto.order())
                    .target(dto.target())
                    .metric(dto.metric())
                    .descriptionBefore(dto.descriptionBefore())
                    .descriptionAfter(dto.descriptionAfter())
                    .objective(objective)
                    .build());
            if (dto.taskList() != null) {
                taskRepository.saveAll(dto.taskList().stream().map((task) -> Task.builder()
                        .title(task.title())
                        .order(task.order())
                        .keyResult(keyResult)
                        .build()).toList());
            }
        }
    }

    @Transactional
    public void create(KeyResultCreateRequestDto request) {
        Objective objective = objectiveRepository.findById(request.objectiveId())
                .orElseThrow(ObjectiveNotFoundException::new);
        List<KeyResult> krList = keyResultRepository.findAllByObjective(objective);

        if (krList.size() + request.krList().size() > ACTIVE_KEY_RESULT_NUMBER) {
            throw new KeyResultNumberExceededException();
        }

        for (KeyResultCreateRequestInfoDto dto : request.krList()) {
            for (short i = dto.order(); i < krList.size(); i++) {
                krList.get(i).incrementOrder();
            }
            KeyResult keyResult = keyResultRepository.save(KeyResult.builder()
                    .title(dto.title())
                    .period(Period.of(dto.startAt(), dto.expireAt()))
                    .order(dto.order())
                    .target(dto.target())
                    .metric(dto.metric())
                    .descriptionBefore(dto.descriptionBefore())
                    .descriptionAfter(dto.descriptionAfter()).build());
            if (dto.taskList() != null) {
                taskRepository.saveAll(
                        dto.taskList().stream().map((task) -> Task.builder()
                        .title(task.title())
                        .order(task.order())
                        .keyResult(keyResult)
                        .build()).toList());
            }
        }
    }

    @Transactional
    public void delete(List<Long> keyResultIds) {
        cascadeDelete(keyResultRepository.findByIdIn(keyResultIds));
    }

    @Transactional
    public void delete(Objective objective) {
        cascadeDelete(keyResultRepository.findAllByObjective(objective));
    }

    private void cascadeDelete(List<KeyResult> krList) {
        krList.forEach((kr) -> {
            taskRepository.deleteAllInBatch(taskRepository.findAllByKeyResult(kr));
        });
        keyResultRepository.deleteAllInBatch(krList);
    }

}
