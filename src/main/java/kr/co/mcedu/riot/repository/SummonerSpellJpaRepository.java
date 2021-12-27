package kr.co.mcedu.riot.repository;

import kr.co.mcedu.riot.entity.SummonerSpellEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummonerSpellJpaRepository extends JpaRepository<SummonerSpellEntity,Long> {
}
