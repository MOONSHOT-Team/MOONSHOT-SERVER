package org.moonshot.server.domain.task.repository;

import org.moonshot.server.domain.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
