package org.moonshot.server.domain.objective.repository;

import static org.moonshot.server.domain.keyresult.model.QKeyResult.*;
import static org.moonshot.server.domain.objective.model.QObjective.*;
import static org.moonshot.server.domain.task.model.QTask.*;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.server.domain.keyresult.model.QKeyResult;
import org.moonshot.server.domain.objective.dto.request.ObjectiveHistoryRequestDto;
import org.moonshot.server.domain.objective.dto.response.DashboardResponseDto;
import org.moonshot.server.domain.objective.dto.response.HistoryResponseDto;
import org.moonshot.server.domain.objective.model.Category;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.domain.objective.model.QObjective;
import org.moonshot.server.domain.task.model.QTask;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
public class ObjectiveCustomRepositoryImpl implements ObjectiveCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public HistoryResponseDto findObjectives(ObjectiveHistoryRequestDto request) {
        List<Objective> objectives = queryFactory.selectFrom(objective).distinct()
                .join(objective.keyResultList, keyResult).fetchJoin()
                .join(keyResult.taskList, task)
                .where(yearEq(request.year()), categoryEq(request.category()))
                .orderBy(order(request.criteria()), keyResult.idx.asc())
                .fetch();

        Map<Integer, List<Objective>> groups = objectives.stream()
                .collect(Collectors.groupingBy(objective -> objective.getPeriod().getStartAt().getYear()));

        List<String> categories = objectives.stream().map(objective -> objective.getCategory().getValue()).toList();

        return HistoryResponseDto.of(groups, categories);
    }

    private BooleanExpression yearEq(Integer year) {
        return year != null ? objective.period.expireAt.year().eq(year) : null;
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? objective.category.eq(category) : null;
    }

    private OrderSpecifier<?> order(String orderBy) {
        OrderSpecifier<?> orderSpecifier;

        if (orderBy == null) {
            return objective.period.startAt.asc();
        }

        switch (orderBy) {
            case "LATEST":
                orderSpecifier = objective.period.startAt.asc();
                break;
            case "OLDEST":
                orderSpecifier = objective.period.startAt.desc();
                break;
            case "ACCOMPLISH":
                orderSpecifier = objective.progress.desc();
            default:
                orderSpecifier = objective.period.startAt.asc();
                break;
        }
        return orderSpecifier;
    }

}
