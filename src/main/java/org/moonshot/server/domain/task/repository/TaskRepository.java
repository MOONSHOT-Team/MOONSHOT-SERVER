package org.moonshot.server.domain.task.repository;

import java.util.List;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByKeyResult(KeyResult keyResult);

}
