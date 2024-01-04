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
    public void createInitWithObjective(Objective objective, List<KRCreateRequestDto> requests) {
        for (KRCreateRequestDto it : requests) {
            KeyResult keyResult = keyResultRepository.save(KeyResult.builder()
                    .title(it.title())
                    .period(Period.of(it.startAt(), it.expireAt()))
                    .target(it.target())
                    .metric(it.metric())
                    .descriptionBefore(it.descriptionBefore())
                    .descriptionAfter(it.descriptionAfter())
                    .objective(objective)
                    .build());
            taskRepository.saveAll(it.taskList().stream().map((task) -> Task.builder()
                    .title(task.title())
                    .keyResult(keyResult)
                    .build()).toList());
        }
    }

}
