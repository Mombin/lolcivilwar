package kr.co.mcedu.match.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.mcedu.group.entity.CustomUserEntity;
import kr.co.mcedu.group.model.GroupSeasonDto;
import kr.co.mcedu.match.entity.IngameRuneEntity;
import kr.co.mcedu.match.entity.MatchAttendeesEntity;
import kr.co.mcedu.match.entity.MatchBanChamp;
import kr.co.mcedu.match.entity.MatchPickChampionEntity;
import kr.co.mcedu.match.model.CustomMatchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.bean;
import static kr.co.mcedu.group.entity.QCustomUserEntity.customUserEntity;
import static kr.co.mcedu.group.entity.QGroupSeasonEntity.groupSeasonEntity;
import static kr.co.mcedu.match.entity.QCustomMatchEntity.customMatchEntity;
import static kr.co.mcedu.match.entity.QMatchAttendeesEntity.matchAttendeesEntity;

@Repository
@RequiredArgsConstructor
public class MatchRepository {
    private JPAQueryFactory queryFactory;
    private final EntityManager entityManager;
    @PostConstruct
    public void init() {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 해당 유저가 참여한 참여 목록 가져오기
     * @param entity customUserEntity
     * @param seasonSeq
     * @return 참여 목록
     */
    public List<MatchAttendeesEntity> findAllByCustomUserEntityWithSeasonSeq(CustomUserEntity entity, Long seasonSeq) {
        return queryFactory.select(matchAttendeesEntity)
                           .from(matchAttendeesEntity)
                           .innerJoin(matchAttendeesEntity.customMatch, customMatchEntity).fetchJoin()
                           .where(matchAttendeesEntity.customUserEntity.eq(entity),
                                   customMatchEntity.groupSeason.groupSeasonSeq.eq(seasonSeq))
                           .fetch();
    }

    /**
     * 매치순번을 이용하여 참여목록 가져오기
     * @param matchSeqs 매치순번
     * @return 참여 목록
     */
    public List<MatchAttendeesEntity> findAllByCustomMatchs(final List<Long> matchSeqs) {
        return queryFactory.select(matchAttendeesEntity)
                           .from(matchAttendeesEntity)
                           .innerJoin(matchAttendeesEntity.customUserEntity, customUserEntity).fetchJoin()
                           .where(matchAttendeesEntity.customMatch.matchSeq.in(matchSeqs)).fetch();
    }

    public void saveBanChmpions(List<MatchBanChamp> banChampList) {
        banChampList.forEach(entityManager::persist);
    }

    public void save(final MatchPickChampionEntity matchPickChampionEntity) {
        entityManager.persist(matchPickChampionEntity);
    }

    public void saveIngameRunes(List<IngameRuneEntity> ingameRuneEntities) {
        ingameRuneEntities.forEach(entityManager::persist);
    }

    public Page<CustomMatchDto> findByGroup_GroupSeqOrderByMatchSeqDesc(Long groupSeq, Pageable pageable) {
        BooleanBuilder condition = new BooleanBuilder().andAnyOf(customMatchEntity.delYn.eq(false),
                customMatchEntity.group.groupSeq.eq(groupSeq));


        long count = queryFactory.select(customMatchEntity.matchSeq)
                                 .from(customMatchEntity)
                                 .where(condition)
                                 .fetchCount();

        List<CustomMatchDto> results = queryFactory.from(customMatchEntity)
                                                   .leftJoin(groupSeasonEntity).on(customMatchEntity.groupSeason.eq(groupSeasonEntity))
                                                   .where(condition)
                                                   .orderBy(customMatchEntity.matchSeq.desc())
                                                   .limit(pageable.getPageSize())
                                                   .offset(pageable.getOffset())
                                                   .transform(groupBy(customMatchEntity.matchSeq)
                                                           .list(bean(CustomMatchDto.class,
                                                                   customMatchEntity.matchSeq,
                                                                   customMatchEntity.createdDate,
                                                                   bean(GroupSeasonDto.class,
                                                                           groupSeasonEntity.seasonName)
                                                                           .as("groupSeason"))
                                                           )
                                                   );

        return new PageImpl<>(results, pageable, count);
    }
}
