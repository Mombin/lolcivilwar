package kr.co.mcedu.riot.repository;

import kr.co.mcedu.riot.entity.ChampionDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampionJpaRepository extends JpaRepository<ChampionDataEntity,Long> {
}
