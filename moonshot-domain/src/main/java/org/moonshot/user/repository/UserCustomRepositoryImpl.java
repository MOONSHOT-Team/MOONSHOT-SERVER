package org.moonshot.user.repository;

import static org.moonshot.user.model.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.moonshot.user.model.User;
import org.springframework.cache.annotation.Cacheable;

@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    @Cacheable(value = "user", cacheManager = "redisCacheManager")
    public Optional<User> findByIdWithCache(final Long id) {
        return Optional.ofNullable(queryFactory.selectFrom(user)
                .where(user.id.eq(id)).fetchOne());
    }

}
