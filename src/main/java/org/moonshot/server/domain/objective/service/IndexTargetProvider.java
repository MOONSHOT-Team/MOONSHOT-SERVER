package org.moonshot.server.domain.objective.service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.keyresult.service.KeyResultService;
import org.moonshot.server.domain.objective.model.IndexService;
import org.moonshot.server.domain.objective.model.IndexTarget;
import org.moonshot.server.domain.task.service.TaskService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IndexTargetProvider {

    private static final Map<IndexTarget, IndexService> indexServiceMap = new HashMap<>();
    private final ObjectiveService objectiveService;
    private final KeyResultService keyResultService;
    private final TaskService taskService;

    @PostConstruct
    void initIndexServiceMap() {
        indexServiceMap.put(IndexTarget.KEY_RESULT, keyResultService);
        indexServiceMap.put(IndexTarget.TASK, taskService);
        indexServiceMap.put(IndexTarget.OBJECTIVE, objectiveService);
    }

    public IndexService getIndexService(final IndexTarget indexTarget) {
        return indexServiceMap.get(indexTarget);
    }

}
