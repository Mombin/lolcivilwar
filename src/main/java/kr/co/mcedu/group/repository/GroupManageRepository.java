package kr.co.mcedu.group.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.mcedu.group.entity.CustomUserEntity;
import kr.co.mcedu.match.entity.MatchAttendeesEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import static kr.co.mcedu.match.entity.QCustomMatchEntity.customMatchEntity;
import static kr.co.mcedu.match.entity.QMatchAttendeesEntity.matchAttendeesEntity;

@Repository
@RequiredArgsConstructor
public class GroupManageRepository {
    private final EntityManager entityManager;
    private JPAQueryFactory queryFactory;
    @PostConstruct
    public void init() {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    public Page<MatchAttendeesEntity> findAllPersonalMatchResult(CustomUserEntity entity, Pageable pageable) {
        QueryResults<MatchAttendeesEntity> results =
                queryFactory.select(matchAttendeesEntity).distinct()
                            .from(matchAttendeesEntity)
                            .leftJoin(matchAttendeesEntity.customMatch, customMatchEntity).fetchJoin()
                            .where(matchAttendeesEntity.customUserEntity.eq(entity))
                            .orderBy(matchAttendeesEntity.createdDate.desc())
                            .offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}