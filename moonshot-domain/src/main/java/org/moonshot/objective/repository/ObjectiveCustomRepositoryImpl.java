package org.moonshot.objective.repository;

import static org.moonshot.keyresult.model.QKeyResult.keyResult;
import static org.moonshot.objective.model.QObjective.objective;
import static org.moonshot.task.model.QTask.task;
import static org.moonshot.user.model.QUser.user;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.moonshot.keyresult.model.QKeyResult;
import org.moonshot.objective.model.Category;
import org.moonshot.objective.model.Criteria;
import org.moonshot.objective.model.Objective;
import org.moonshot.task.model.QTask;
import org.moonshot.user.model.QUser;

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
            return objective.period.startAt.desc();
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

    @Override
    public List<Objective> findSocialObjectives() {
        return queryFactory.selectFrom(objective)
                .join(objective.user, user).fetchJoin()
                .leftJoin(objective.keyResultList, keyResult).fetchJoin()
                .leftJoin(keyResult.taskList, task)
                .where(objective.isPublic.eq(true))
                .orderBy(objective.heartCount.desc(), objective.id.desc(), keyResult.idx.asc(), task.idx.asc())
                .limit(10)
                .fetch();
    }

}
