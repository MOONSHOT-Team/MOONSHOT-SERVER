package org.moonshot.server.domain.objective.repository;

import static org.moonshot.server.domain.keyresult.model.QKeyResult.*;
import static org.moonshot.server.domain.objective.model.QObjective.*;
import static org.moonshot.server.domain.task.model.QTask.*;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.server.domain.objective.model.Category;
import org.moonshot.server.domain.objective.model.Criteria;
import org.moonshot.server.domain.objective.model.Objective;

@Slf4j
@RequiredArgsConstructor
public class ObjectiveCustomRepositoryImpl implements ObjectiveCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Objective> findObjectives(Long userId, Integer year, Category category, Criteria criteria) {
        return queryFactory.selectFrom(objective).distinct()
                .join(objective.keyResultList, keyResult).fetchJoin()
                .join(keyResult.taskList, task)
                .where(objective.isClosed.eq(true), userEq(userId), yearEq(year), categoryEq(category))
                .orderBy(order(criteria), keyResult.idx.asc())
                .fetch();
    }

    private BooleanExpression userEq(Long userId) {
        return userId != null ? objective.user.id.eq(userId) : null;
    }

    private BooleanExpression yearEq(Integer year) {
        return year != null ? objective.period.startAt.year().eq(year) : null;
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? objective.category.eq(category) : null;
    }

    private OrderSpecifier<?> order(Criteria criteria) {
        OrderSpecifier<?> orderSpecifier;

        if (criteria == null) {
            return objective.period.startAt.asc();
        }

        switch (criteria) {
            case LATEST:
                orderSpecifier = objective.period.startAt.desc();
                break;
            case OLDEST:
                orderSpecifier = objective.period.startAt.asc();
                break;
            case ACCOMPLISH:
                orderSpecifier = objective.progress.desc();
                break;
            default:
                orderSpecifier = objective.period.startAt.desc();
                break;
        }
        return orderSpecifier;
    }

}
