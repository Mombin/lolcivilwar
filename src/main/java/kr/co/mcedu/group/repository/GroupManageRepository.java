package kr.co.mcedu.group.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.mcedu.group.entity.CustomUserEntity;
import kr.co.mcedu.group.entity.GroupEntity;
import kr.co.mcedu.match.entity.MatchAttendeesEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.*;

import static kr.co.mcedu.group.entity.QCustomUserEntity.customUserEntity;
import static kr.co.mcedu.group.entity.QGroupEntity.groupEntity;
import static kr.co.mcedu.match.entity.QCustomMatchEntity.customMatchEntity;
import static kr.co.mcedu.match.entity.QMatchAttendeesEntity.matchAttendeesEntity;
import static kr.co.mcedu.summoner.entity.QSummonerEntity.summonerEntity;

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

    public Optional<GroupEntity> findByIdFetch(Long groupSeq) {
        if (Objects.isNull(groupSeq)) {
            return Optional.empty();
        }

        return Optional.ofNullable(queryFactory.select(groupEntity).distinct()
                                               .from(groupEntity)
                                               .leftJoin(groupEntity.customUser, customUserEntity).fetchJoin()
                                               .leftJoin(customUserEntity.summonerEntity, summonerEntity).fetchJoin()
                                               .where(groupEntity.groupSeq.eq(groupSeq))
                                               .fetchOne());
    }

    public Optional<CustomUserEntity> customUserFetch(Long customUserSeq) {
        if (Objects.isNull(customUserSeq)) {
            return Optional.empty();
        }

        return Optional.ofNullable(queryFactory.select(customUserEntity)
                                               .from(customUserEntity)
                                               .leftJoin(customUserEntity.summonerEntity, summonerEntity).fetchJoin()
                                               .where(customUserEntity.seq.eq(customUserSeq))
                                               .fetchOne());
    }

    public List<GroupEntity> getGroupEntities(Collection<Long> groupSeqs) {
        if (groupSeqs.isEmpty()) {
            return Collections.emptyList();
        }

        return queryFactory.select(groupEntity).distinct()
                           .from(groupEntity)
                           .leftJoin(groupEntity.customUser, customUserEntity).fetchJoin()
                           .leftJoin(customUserEntity.summonerEntity, summonerEntity).fetchJoin()
                           .where(groupEntity.groupSeq.in(groupSeqs)).fetch();
    }

    public List<MatchAttendeesEntity> getMatchAttendees(Collection<Long> matchSeqs) {
        if (matchSeqs.isEmpty()) {
            return Collections.emptyList();
        }

        return queryFactory.selectFrom(matchAttendeesEntity)
                           .where(matchAttendeesEntity.customMatch.matchSeq.in(matchSeqs)).fetch();
    }
}