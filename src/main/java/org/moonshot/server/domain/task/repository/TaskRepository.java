package org.moonshot.server.domain.task.repository;

import java.util.List;
import java.util.Optional;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByKeyResult(KeyResult keyResult);
    List<Task> findAllByKeyResultOrderByIdx(KeyResult keyResult);
    @Query("select count(t) from Task t join t.keyResult kr where kr.id = :keyResultId")
    Long countAllByKeyResultId(@Param("keyResultId") Long keyResultId);
    @Query("select t from Task t join fetch t.keyResult kr join fetch kr.objective obj join fetch obj.user u where t.id = :taskId")
    Optional<Task> findTaskWithFetchJoin(@Param("taskId") Long taskId);
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Task t SET t.idx = t.idx + 1 WHERE t.idx >= :lBound AND t.idx < :uBound AND t.keyResult.id = :keyResultId AND t.id != :targetId")
    void bulkUpdateTaskIdxIncrease(@Param("lBound") int lowerBound, @Param("uBound") int upperBound, @Param("keyResultId") Long keyResultId, @Param("targetId") Long targetId);
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Task t SET t.idx = t.idx - 1 WHERE t.idx >= :lBound AND t.idx <= :uBound AND t.keyResult.id = :keyResultId AND t.id != :targetId")
    void bulkUpdateTaskIdxDecrease(@Param("lBound") int lowerBound, @Param("uBound") int upperBound, @Param("keyResultId") Long keyResultId, @Param("targetId") Long targetId);

}
