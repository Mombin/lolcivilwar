package kr.co.mcedu.group.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.mcedu.group.entity.CustomUserEntity;
import kr.co.mcedu.group.entity.GroupAuthEntity;
import kr.co.mcedu.group.entity.GroupEntity;
import kr.co.mcedu.group.entity.GroupSeasonEntity;
import kr.co.mcedu.group.model.response.CustomUserResponse;
import kr.co.mcedu.match.entity.CustomMatchEntity;
import kr.co.mcedu.match.entity.MatchAttendeesEntity;
import kr.co.mcedu.user.entity.GroupInviteEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;

import static kr.co.mcedu.group.entity.QCustomUserEntity.customUserEntity;
import static kr.co.mcedu.group.entity.QGroupAuthEntity.groupAuthEntity;
import static kr.co.mcedu.group.entity.QGroupEntity.groupEntity;
import static kr.co.mcedu.group.entity.QGroupSeasonEntity.groupSeasonEntity;
import static kr.co.mcedu.match.entity.QCustomMatchEntity.customMatchEntity;
import static kr.co.mcedu.match.entity.QMatchAttendeesEntity.matchAttendeesEntity;
import static kr.co.mcedu.summoner.entity.QSummonerEntity.summonerEntity;
import static kr.co.mcedu.user.entity.QGroupInviteEntity.groupInviteEntity;
import static kr.co.mcedu.user.entity.QWebUserEntity.webUserEntity;

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

    public Optional<GroupAuthEntity> getGroupAuthByGroupSeqAndUserSeq(Long groupSeq, Long userSeq) {
        if (userSeq == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(queryFactory.select(groupAuthEntity).from(groupAuthEntity)
                                               .where(groupAuthEntity.group.groupSeq.eq(groupSeq),
                                                       groupAuthEntity.webUser.userSeq.eq(userSeq))
                                               .fetchOne());
    }

    public void delete(final GroupAuthEntity groupAuthEntity) {
        entityManager.remove(groupAuthEntity);
    }

    public List<MatchAttendeesEntity> getMatchAttendees(Collection<Long> matchSeqs) {
        if (matchSeqs.isEmpty()) {
            return Collections.emptyList();
        }

        return queryFactory.selectFrom(matchAttendeesEntity)
                           .where(matchAttendeesEntity.customMatch.matchSeq.in(matchSeqs)).fetch();
    }

    public List<GroupSeasonEntity> getGroupSeasonsByGroupSeqs(Collection<Long> groupSeqs) {
        if (groupSeqs.isEmpty()) {
            return Collections.emptyList();
        }
        return queryFactory.selectFrom(groupSeasonEntity).where(groupSeasonEntity.group.groupSeq.in(groupSeqs)).fetch();
    }

    public GroupSeasonEntity save(final GroupSeasonEntity groupSeasonEntity) {
        return entityManager.merge(groupSeasonEntity);
    }

    public void updateAllGroupSeason(GroupSeasonEntity groupSeasonEntity) {
        queryFactory.update(customMatchEntity).set(customMatchEntity.groupSeason, groupSeasonEntity)
                    .where(customMatchEntity.group.eq(groupSeasonEntity.getGroup()))
                    .execute();
    }

    public List<GroupSeasonEntity> getGroupSeasons(final Set<Long> seasonSeqs) {
        return queryFactory.selectFrom(groupSeasonEntity).where(groupSeasonEntity.groupSeasonSeq.in(seasonSeqs)).fetch();
    }

    public List<CustomMatchEntity> getCustomMatchByGroupSeqAndSeasonSeq(long groupSeq, Long seasonSeq) {
        return queryFactory.selectFrom(customMatchEntity).where(customMatchEntity.group.groupSeq.eq(groupSeq),
                customMatchEntity.groupSeason.groupSeasonSeq.eq(seasonSeq)).fetch();
    }

    public List<CustomUserResponse> getMatchAttendeesByGroupSeqAndSeasonSeq(final Long seasonSeq) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (seasonSeq != -1) {
            booleanBuilder.and(customMatchEntity.groupSeason.groupSeasonSeq.eq(seasonSeq));
        }

        return queryFactory.select(Projections.bean(CustomUserResponse.class, customUserEntity.nickname, customUserEntity.summonerName, customUserEntity.seq))
                           .distinct()
                           .from(customMatchEntity)
                           .innerJoin(matchAttendeesEntity).on(matchAttendeesEntity.customMatch.matchSeq.eq(customMatchEntity.matchSeq))
                           .innerJoin(customUserEntity).on(matchAttendeesEntity.customUserEntity.eq(customUserEntity), customUserEntity.delYn.eq(false))
                           .where(booleanBuilder)
                           .fetch();
    }

    public GroupInviteEntity save(GroupInviteEntity groupInviteEntity) {
        return entityManager.merge(groupInviteEntity);
    }

    public GroupAuthEntity save(GroupAuthEntity groupAuthEntity) {
        return entityManager.merge(groupAuthEntity);
    }

    public List<GroupAuthEntity> getGroupAuthByGroupSeq(final Long groupSeq) {
        return queryFactory.selectFrom(groupAuthEntity)
                .innerJoin(groupAuthEntity.webUser, webUserEntity).fetchJoin()
                .where(groupAuthEntity.group.groupSeq.eq(groupSeq))
                .fetch();
    }

    public QueryResults<GroupInviteEntity> getGroupInviteHistory(final Long groupSeq, final Pageable page) {
        return queryFactory.select(groupInviteEntity)
                           .from(groupInviteEntity)
                           .innerJoin(groupInviteEntity.user, webUserEntity).fetchJoin()
                           .innerJoin(groupInviteEntity.invitedUser, webUserEntity).fetchJoin()
                           .where(groupInviteEntity.group.groupSeq.eq(groupSeq))
                           .offset(page.getOffset())
                           .limit(page.getPageSize())
                           .orderBy(groupInviteEntity.invitedDate.desc())
                           .fetchResults();
    }

    public Optional<GroupInviteEntity> getAlreadyInviteCheck(Long groupSeq, Long userSeq) {
        return Optional.ofNullable(queryFactory.select(groupInviteEntity)
                                               .from(groupInviteEntity)
                                               .where(groupInviteEntity.group.groupSeq.eq(groupSeq),
                                                       groupInviteEntity.invitedUser.userSeq.eq(userSeq),
                                                       groupInviteEntity.expireResult.isFalse().or(groupInviteEntity.modifiedDate.after(LocalDateTime.now().minusDays(1))))
                                               .fetchFirst());
    }
}