package kr.co.mcedu.riot.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.mcedu.riot.entity.ChampionDataEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static kr.co.mcedu.riot.entity.QChampionDataEntity.championDataEntity;

@Repository
@RequiredArgsConstructor
public class RiotDataRepository {
    private final EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<ChampionDataEntity> getChampions() {
        return queryFactory.selectFrom(championDataEntity).fetch();
    }

    public Optional<ChampionDataEntity> getChampion(final Long championId) {
        if (championId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(queryFactory.selectFrom(championDataEntity)
                                               .where(championDataEntity.championId.eq(championId))
                                               .fetchFirst());
    }
}
