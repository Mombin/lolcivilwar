package kr.co.mcedu.group.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.mcedu.group.model.request.MostChampionRequest;
import kr.co.mcedu.group.model.response.MostChampionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;

import static kr.co.mcedu.match.entity.QCustomMatchEntity.customMatchEntity;
import static kr.co.mcedu.match.entity.QMatchAttendeesEntity.matchAttendeesEntity;
import static kr.co.mcedu.match.entity.QMatchPickChampionEntity.matchPickChampionEntity;
import static kr.co.mcedu.riot.entity.QChampionDataEntity.championDataEntity;

@Repository
@RequiredArgsConstructor
public class MatchDataRepository {
    private final EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<MostChampionResponse> findMostChampion(MostChampionRequest request){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(request.getCustomUserSeq() != -1) {
            booleanBuilder.and(matchAttendeesEntity.customUserEntity.seq.eq(request.getCustomUserSeq()));
        }
        return queryFactory.select(Projections.bean(
                MostChampionResponse.class,
                        championDataEntity.championKoreaName,
                        matchAttendeesEntity.matchResult,
                        matchAttendeesEntity.createdDate,
                        championDataEntity.championId))
                .from(matchPickChampionEntity)
                .innerJoin(matchAttendeesEntity).on(matchAttendeesEntity.attendeesSeq.eq(matchPickChampionEntity.attendeesSeq), matchAttendeesEntity.position.eq(request.getPosition()), matchAttendeesEntity.delYn.eq(false))
                .innerJoin(customMatchEntity).on(customMatchEntity.matchSeq.eq(matchAttendeesEntity.customMatch.matchSeq), customMatchEntity.delYn.eq(false), customMatchEntity.groupSeason.groupSeasonSeq.eq(request.getGroupSeasonSeq()))
                .innerJoin(championDataEntity).on(matchPickChampionEntity.pickChampId.eq(championDataEntity.championId))
                .where(booleanBuilder)
//                .where(matchAttendeesEntity.customUserEntity.seq.eq(request.getCustomUserSeq()))
                .orderBy(matchAttendeesEntity.createdDate.desc())
                .fetch();
    }
}
